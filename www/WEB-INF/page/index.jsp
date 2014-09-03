<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>${APP_TITLE}</title>
<jsp:include page="include/meta.jsp" />
</head>
<body id="index">
<jsp:include page="login.jsp" />
<jsp:include page="menu.jsp" />
<div id="w" class="easyui-panel" data-options="title:'',inline:true,closed:true" style="padding:10px">  
</div>
<script type="text/javascript">
(function($) {

  function reset()
  {
    $.MENU.close();
    $.LOGIN.open();
    $('#w').panel('close');
  }

  function start()
  {
    $.LOGIN.close();
    $.MENU.open();
    $('#w').panel('open');
    $('#w').panel('maximize');
  }

  function _logout()
  {
    var fs = function(result) {
      APP_ACC = result.APP_ACC;
      APP_EVENTS.trigger('account', 'logout');
    };
    $.ajax({
      type: 'POST',
      contentType: 'application/json',
      url: APP_CONTEXT_PATH + '/account/logout',
      dataType: 'json',
      success: fs
    });
  }

  function _extend(mi)
  {
    var p = mi, r = {};
    while (p != null) {
      for (var n in p) if (n != 'children' && !r[n]) r[n] = p[n];
      // r = $.extend({}, p, r);
      p = p.parent();
    }
    return r;
  }

  function _onAccount(code)
  {
    if (code == 'login') { // 登录成功
      start(); // 
    } else if (code == 'logout') { // 注销
      reset();
    } else if (code == 'forget') { // 忘记密码
    } else if (code == 'signup') { // 注册
    }
  }

  function _onMenu(mi)
  {
    var f1 = function(d, mi) {
      var pmi = mi.parent();
      var opts = $('#w').panel('options');
      var f1e = function() {if (d.event) APP_EVENTS.trigger(d.event, mi);};
      if (d.url && opts.href != d.url) {
        $('#w').panel({
          title:d.title,
          href:d.url,
          onLoad:f1e
        });
      } else {
      	f1e();
      }
    };
    var p, d, s = '';
    d = _extend(mi);
    if (d.event == 'logout') {
      _logout();
    } else if (d.event == 'test') {
      f1(d, mi);
    } else {
      p = mi;
      while (p != null) {
        if (p.hasOwnProperty('text')) s = '\\' + p.text + s;
        p = p.parent();
      }
      $.messager.alert(APP_TITLE, s, 'info');
    }
    // $.messager.show({title:APP_TITLE, msg:event, showType:null, timeout:2000});
  }

  $.INDEX = $.INDEX || {version: '1.0.0', name: 'INDEX'};
  $.extend($.INDEX, {
    reset:reset,
    start:start
  });
  APP_EVENTS.on('account', _onAccount);
  APP_EVENTS.on('menu', _onMenu);
})(jQuery);

$(function() {
  if (!APP_ACC) $.INDEX.reset();
  else $.INDEX.start();
});
</script>
</body>
</html>