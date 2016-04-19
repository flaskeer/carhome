package com.hao.util.xss;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

/**
 * 防止跨站脚本攻击
 * Created by user on 2016/4/19.
 */
public class XssFilterWrapper extends HttpServletRequestWrapper {

    private static final List<Pattern> PATTERNS = new CopyOnWriteArrayList<>();

    static {
        PATTERNS.add(Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE));
        PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        PATTERNS.add(Pattern.compile("</script>", Pattern.CASE_INSENSITIVE));
        PATTERNS.add(Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        PATTERNS.add(Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        PATTERNS.add(Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
        PATTERNS.add(Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE));
        PATTERNS.add(Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE));
        PATTERNS.add(Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
    }
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XssFilterWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return new String[0];
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXss(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (value == null) {
            return null;
        }
        return cleanXss(value);
    }

    private String cleanXss(String value) {
        if (value != null) {
            for (Pattern pattern : PATTERNS) {
                value = pattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
}
