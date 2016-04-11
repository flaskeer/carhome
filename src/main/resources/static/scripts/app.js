/**
 * HOMER - Responsive Admin Theme
 * version 1.9
 *
 */

$(document).ready(function () { // NOSONAR

  // Add special class to minimalize page elements when screen is less than 768px
  setBodySmall();

  // Handle minimalize sidebar menu
  $('.hide-menu').on('click', function (event) {
    event.preventDefault();
    if ($(window).width() < 769) {
      $("body").toggleClass("show-sidebar");
    } else {
      $("body").toggleClass("hide-sidebar");
    }
  });

  // Initialize metsiMenu plugin to sidebar menu
  $('#side-menu').metisMenu();

  // Initialize iCheck plugin
  $('.i-checks').iCheck({
    checkboxClass: 'icheckbox_square-green',
    radioClass: 'iradio_square-green'
  });

  // Initialize animate panel function
  $('.animate-panel').animatePanel();

  // Function for collapse hpanel
  $('.showhide').on('click', function (event) {
    event.preventDefault();
    var hpanel = $(this).closest('div.hpanel');
    var icon = $(this).find('i:first');
    var body = hpanel.find('div.panel-body');
    var footer = hpanel.find('div.panel-footer');
    body.slideToggle(300);
    footer.slideToggle(200);

    // Toggle icon from up to down
    icon.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
    hpanel.toggleClass('').toggleClass('panel-collapse');
    setTimeout(function () {
      hpanel.resize();
      hpanel.find('[id^=map-]').resize();
    }, 50);
  });

  // Function for close hpanel
  $('.closebox').on('click', function (event) {
    event.preventDefault();
    var hpanel = $(this).closest('div.hpanel');
    hpanel.remove();
    if ($('body').hasClass('fullscreen-panel-mode')) {
      $('body').removeClass('fullscreen-panel-mode');
    }
  });

  // Fullscreen for fullscreen hpanel
  $('.fullscreen').on('click', function () {
    var hpanel = $(this).closest('div.hpanel');
    var icon = $(this).find('i:first');
    $('body').toggleClass('fullscreen-panel-mode');
    icon.toggleClass('fa-expand').toggleClass('fa-compress');
    hpanel.toggleClass('fullscreen');
    setTimeout(function () {
      $(window).trigger('resize');
    }, 100);
  });

  // Open close right sidebar
  $('.right-sidebar-toggle').on('click', function () {
    $('#right-sidebar').toggleClass('sidebar-open');
  });

  // Function for small header
  $('.small-header-action').on('click', function (event) {
    event.preventDefault();
    var icon = $(this).find('i:first');
    var breadcrumb = $(this).parent().find('#hbreadcrumb');
    $(this).parent().parent().parent().toggleClass('small-header');
    breadcrumb.toggleClass('m-t-lg');
    icon.toggleClass('fa-arrow-up').toggleClass('fa-arrow-down');
  });

  // Set minimal height of #wrapper to fit the window
  setTimeout(function () {
    fixWrapperHeight();
  });


  // Initialize tooltips
  $('.tooltip-demo').tooltip({
    selector: "[data-toggle=tooltip]"
  });

  // Initialize popover
  $("[data-toggle=popover]").popover();

  // Move modal to body
  // Fix Bootstrap backdrop issu with animation.css
  $('.modal').appendTo("body");
});

$(window).bind("load", function () {
  // Remove splash screen after load
  $('.splash').css('display', 'none');
});

$(window).bind("resize click", function () {

  // Add special class to minimalize page elements when screen is less than 768px
  setBodySmall();

  // Waint until metsiMenu, collapse and other effect finish and set wrapper height
  setTimeout(function () {
    fixWrapperHeight();
  }, 300);
});

function fixWrapperHeight() {

  // Get and set current height
  var headerH = 62;
  var navigationH = $("#navigation").height();
  var contentH = $(".content").height();

  // Set new height when contnet height is less then navigation
  if (contentH < navigationH) {
    $("#wrapper").css("min-height", navigationH + 'px');
  }

  // Set new height when contnet height is less then navigation and navigation is less then window
  if (contentH < navigationH && navigationH < $(window).height()) {
    $("#wrapper").css("min-height", $(window).height() - headerH + 'px');
  }

  // Set new height when contnet is higher then navigation but less then window
  if (contentH > navigationH && contentH < $(window).height()) {
    $("#wrapper").css("min-height", $(window).height() - headerH + 'px');
  }
}


function setBodySmall() {
  if ($(this).width() < 769) {
    $('body').addClass('page-small');
  } else {
    $('body').removeClass('page-small').removeClass('show-sidebar');
  }
}

// Animate panel function
$.fn['animatePanel'] = function () {

  var element = $(this);
  var effect = $(this).data('effect');
  var delay = $(this).data('delay');
  var child = $(this).data('child');

  // Set default values for attrs
  if (!effect) {
    effect = 'zoomIn';
  }
  if (!delay) {
    delay = 0.06;
  } else {
    delay = delay / 10;
  }
  if (!child) {
    child = '.row > div';
  } else {
    child = "." + child;
  }

  //Set defaul values for start animation and delay
  var startAnimation = 0;
  var start = Math.abs(delay) + startAnimation;

  // Get all visible element and set opacity to 0
  var panel = element.find(child);
  panel.addClass('opacity-0');

  // Get all elements and add effect class
  panel = element.find(child);
  panel.addClass('stagger').addClass('animated-panel').addClass(effect);

  var panelsCount = panel.length + 10;
  var animateTime = (panelsCount * delay * 10000) / 10;

  // Add delay for each child elements
  panel.each(function (i, elm) {
    start += delay;
    var rounded = Math.round(start * 10) / 10;
    $(elm).css('animation-delay', rounded + 's');
    // Remove opacity 0 after finish
    $(elm).removeClass('opacity-0');
  });

  // Clear animation after finish
  setTimeout(function () {
    $('.stagger').css('animation', '').removeClass(effect).removeClass('animated-panel').removeClass('stagger');
  }, animateTime);
};
