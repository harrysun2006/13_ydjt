<%@page contentType="text/html; charset=utf-8"%>
<div id="login_win" class="easyui-window" title="用户登录" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,closable:false,closed:true,iconCls:'icon-login'" style="width:300px">
<div style="padding:10px 0 10px 60px">
<form id="login_form" method="post">
<table>
	<tr>
		<td>用户名:</td>
		<td><input class="easyui-validatebox" type="text" id="login_username" name="login_username" data-options="required:true"></input></td>
	</tr>
	<tr>
		<td>密码:</td>
		<td><input class="easyui-validatebox" type="password" id="login_password" name="login_password"></input></td>
	</tr>
	<tr>
		<td colspan="2">
			<div style="padding:5px;text-align:center;">
      	<a id="_login" href="javascript:void(0)" class="easyui-linkbutton" icon="icon-ok">确定</a>
        <a id="_cancel" href="javascript:void(0)" class="easyui-linkbutton" icon="icon-cancel">取消</a>
     	</div>
		</td>
	</tr> 
</table>
</form>
</div>
</div>
<script type="text/javascript">
(function($) {

  function open()
  {
    $('#login_win').window('open');
  }

  function close()
  {
    $('#login_win').window('close')
  }

	function _login()
	{
    var data = {
      username: $('#login_username').val(),
      password: $('#login_password').val(),
      rememberMe: false
    };
    var json = $.toJSON(data);
    var fs = function(result) {
      APP_ACC = result.APP_ACC;
      APP_EVENTS.trigger('account', 'login');
    };
    $.ajax({
      type: 'POST',
      contentType: 'application/json',
      url: APP_CONTEXT_PATH + '/account/login',
      data: json,
      dataType: 'json',
      success: fs
    });
	}

	function _cancel()
	{
	  // $.messager.alert(APP_TITLE, 'Hello', 'error');
	}

  $.LOGIN = $.LOGIN || {version: '1.0.0', name: 'LOGIN'}; // 包装成类, 更好地控制调用访问接口
  $.extend($.LOGIN, {
    open:open,
    close:close
  });
  $('#_login').unbind('click'); $('#_login').bind('click', _login);
  $('#_cancel').unbind('click'); $('#_cancel').bind('click', _cancel);
})(jQuery);

$(function() {
});
</script>