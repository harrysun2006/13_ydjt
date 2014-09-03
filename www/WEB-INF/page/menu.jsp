<%@page contentType="text/html; charset=utf-8"%>
<div id="menu">
</div>
<script type="text/javascript">
(function($) {

  var MENU_SEP = {'class':'menu-sep'};
  var MAIN_MENU = {name:'root', children:[
		{text:'首页'},
		{icon:'icon-edit', text:'Edit', _style:'width:150px;', children:[
			{icon:'icon-undo', text:'Undo'},
			{icon:'icon-redo', text:'Redo'},
			MENU_SEP,
			{text:'Cut'},
			{text:'Copy'},
			{text:'Paste'},
			MENU_SEP,
			{text:'Toolbar', _style:'width:150px;', children:[
				{text:'Address'},
				{text:'Link'},
				{text:'Navigation Toolbar'},
				{text:'Bookmark Toolbar'},
				MENU_SEP,
				{text:'New Toolbar...'},
				{text:'More', children:[
					{text:'C++'},
					{text:'Java'}
				]}
			]},
			{icon:'icon-remove', text:'Delete'},
			{text:'Select All'}
		]},
		{icon:'icon-help', _style:'width:100px;', text:'帮助', children:[
			{text:'帮助'},
			{text:'升级'},
			{text:'关于'}
		]},
		{text:'关于', class:'menu-content', _style:'background:#f0f0f0;padding:10px;text-align:left', children:[
			{img:'/image/logobd1.jpg', style:'width:150px;height:50px'},
			{style:'font-size:14px;color:#FF0000;', text:'Try jQuery EasyUI to build your modem, interactive, javascript applications.'}
		]},
		{event:'admin', url:'/admin', text:'后台管理', children:[
			{text:'资源管理', children:[
				{text:'课程管理'},
				{text:'教室管理'},
				{text:'讲师管理'},
				{text:'机构管理'},
				{text:'课件管理'}
			]},
			{text:'考评管理', children:[
 				{text:'题库管理'},
 				{text:'试卷管理'},
 				{text:'考试安排'},
 				{text:'作业安排'},
 				{text:'练习安排'},
 				{text:'抽题管理'},
 				{text:'手工评卷'},
 				{text:'成绩发布'}
 			]}
		]},
		{event:'test', url:'/test', text:'测试', title:'测试', children:[
		  {name:'test15', text:'intip', tip:'测试intip控件(itip.js)'},
			{text:'main.js', children:[
				{name:'test01', text:'typeof & getClass'},
				{name:'test02', text:'undefined'},
				{name:'test05', text:'Array::concat & clone'},
				{name:'test18', text:'Array::concat'},
				{name:'test08', text:'Array|Object::for', tip:'可以使用for (var x in any) 来访问数组和对象!'},
				{name:'test11', text:'Array::next & prev'},
				MENU_SEP,
				{name:'test16', text:'Date::format'},
				{name:'test06', text:'Object::clone'},
				{name:'test09', text:'String::empty'},
				{name:'test10', text:'String::repeat & width'},
			]},
			{text:'jQuery', children:[
			  {name:'test17', text:'extend'},
			]},
			{text:'jQuery EasyUI', children:[
				{name:'test07', text:'messager'}
			]},
			{name:'test12', text:'RegEx?Prime/Sum'},
			{text:'Dynamic CSS', children:[
   			{name:'test19', text:'Change href'},
  			{name:'test20', text:'Change style'},
			]},
			{name:'test21', text:'JSON'},
		]},
		{event:'profile', url:'/profile', text:'个人设置'},
		{event:'theme', url:'/theme', text:'切换主题'},
		{event:'logout', text:'退出'}
	]};

  function init()
  {
    var ch = function(e) {
      var mi = e.data;
      APP_EVENTS.trigger('menu', mi);
    };
    var vh = function(mi) {
    };
    $('#menu').buildMenu(MAIN_MENU, {click:ch, visit:vh});
  }

  function open()
  {
    $('#menu').show();
  }

  function close()
  {
    $('#menu').hide();
  }

  $.MENU = $.MENU || {version: '1.0.0', name: 'MENU'};
  $.extend($.MENU, {
    open:open,
    close:close,
    _init:init
  });
})(jQuery);

$(function() {
  $.MENU._init();
});
//$('#btn-edit').menubutton('disable')
//$('#btn-edit').menubutton('enable')
</script>