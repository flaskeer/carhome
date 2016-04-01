package com.hao.util.editor;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2016/4/1.
 */
public class DateEditor extends PropertyEditorSupport{

    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DateFormat dateFormat;
    private boolean allowEmpty = true;

    public DateEditor() {
    }

    public DateEditor(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateEditor(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && StringUtils.hasText(text)) {
            setValue(null);
        } else {
            try {
                if (this.dateFormat != null)
                    setValue(this.dateFormat.parse(text));
                else {
                    setValue(text);
                }
            } catch (ParseException e) {
                throw new IllegalArgumentException("could not parse date:" + e.getMessage(),e);
            }
        }
    }

    @Override
    public String getAsText() {
        Date date = (Date) getValue();
        DateFormat format = this.dateFormat;
        if (format == null)
            format = TIME_FORMAT;
        return date != null ? format.format(date) : "";
    }
}
