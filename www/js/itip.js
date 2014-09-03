(function($) {
$.itip = $.itip || {};
$.extend($.itip,{
  defaults: {
    inColor: '#000000',
    tipColor: '#A8A8A8',
    tip: '请输入内容'
  },
  options: {}
});

$.fn.extend({
  _val: $.fn.val,
  val: function() {
    var opts = $(this).data('#itip.opts');
    if (!arguments.length) {
      var v = $.fn._val.apply(this);
      return opts && opts.tip == v ? '' : v;
    } else {
      if (opts) {
        if (arguments[0] == '') {
          $.fn._val.apply(this, [opts.tip]);
          $.fn.css.apply(this, ['color', opts.tipColor]);
        } else {
          $.fn._val.apply(this, arguments);
          $.fn.css.apply(this, ['color', opts.inColor]);
        }
      } else {
        $.fn._val.apply(this, arguments);
      }
    }
  },
  itip: function(options) {
    return this.each(function() {
      if (options == null) options = '';
      options = typeof(options) == 'string' ? {tip: options} : options;
      var opts = $.extend(false, $.itip.defaults, $.itip.options, options || {});
      var old_opts = $(this).data('#itip.opts');
      $(this).data('#itip.opts', opts);
  
      _fin = function() {
        if ($(this)._val().trim() == opts.tip) $(this)._val('');
        $(this).css('color', opts.inColor);
      };
  
      _ftip = function() {
        var v = $(this)._val().trim();
        if (v == '' || v == opts.tip) {
          $(this)._val(opts.tip);
          $(this).css('color', opts.tipColor);
        }
      };
  
      _f0 = function() {
        if (!old_opts) return;
        if ($(this).val().trim() == old_opts.tip) $(this)._val('');
        $(this).css('color', old_opts.inColor);
      };
  
      if(opts.tip == '') {
        $(this).unbind('focus');
        $(this).unbind('blur');
        _f0.apply(this);
      } else {
        $(this).bind('focus', _fin);
        $(this).bind('blur', _ftip);
        _ftip.apply(this);
      }
    });
  }
});

})(jQuery);