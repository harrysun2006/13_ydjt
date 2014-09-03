<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>${APP_TITLE}</title>
<jsp:include page="include/meta.jsp" />
</head>
<body id="test">
<script type="text/javascript" charset="utf-8" src="${APP_CONTEXT_PATH}/js/ckeditor/ckeditor.js"></script>
<script type="text/javascript" charset="utf-8" src="${APP_CONTEXT_PATH}/js/ckeditor/adapters/jquery.js"></script>
<script type="text/javascript" charset="utf-8" src="${APP_CONTEXT_PATH}/js/ajaxupload.3.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${APP_CONTEXT_PATH}/js/itip.js?ver=${APP_VER}"></script>
<script type="text/javascript" charset="utf-8" src="${APP_CONTEXT_PATH}/js/test.js?ver=${APP_VER}"></script>
User-Agent: ${userAgent}, <%= request.getHeader("User-Agent") %><br>
Content-Type: ${contentType}, <%= response.getContentType() %><br>
<div class="clear"></div>
<div id="info19" class="test">message...</div>
<div class="clear"></div>
<div><textarea id="console" cols="66" rows="3"></textarea></div>
<script type="text/javascript">
(function($) {
  function JSClass()
  {
  }

	function trace(s)
	{
	  console.log(s);
	}
	
	function clear()
	{
	  $('#console').val('');
	}

	// typeof & getClass
	function test01()
	{
		var objs = [new JSClass(), null, true, false, 3, 'Hello World!', [], {}, new Date()];
		var dess = ['new JSClass()', 'null', 'true', 'false', '3', 'Hello World!', '[]', '{}', 'new Date()'];
		trace('====== test01 ======');
		for (var i = 0; i < objs.length; i++) {
		  trace(dess[i] + ': ' + 'typeof -> ' + typeof(objs[i]) + '; getClass -> ' + getClass(objs[i]));
		}
	}

	// undefined
	function test02()
	{
	  var objs = [0, 1, 'Hello', '', null, true, false];
	  var idxs = [];
	  var f1, f2, f3, f4;
	  for (var i = 0; i < objs.length; i++) {
	    idxs.push(i);
	  }
	  idxs.push(99);
	  trace('====== test02 ======');
	  for (var j = 0; j < idxs.length; j++) {
	    i = idxs[j];
	    f1 = objs[i] == null ? 'N' : ' ';
	    f2 = objs[i] == undefined ? 'U' : ' ';
	    f3 = objs[i] ? ' ' : 'F';
	    f4 = typeof(objs[i]) == 'undefined' ? 'X' : ' ';
	    trace(f1 + ',' + f2 + ',' + f3 + ',' + f4 + ' -- objs[' + i + ']=' + objs[i]);
	  }
	}

	// Array: concat & clone
	function test05()
	{
	  var arr1 = [1,2,3];
	  var arr2 = arr1.concat();
	  trace('====== test05 ======');
	  trace('SIMPLE:');
	  trace('arr1: ' + arr1);
	  arr2.push(6);
	  trace('arr1: ' + arr1);
	  trace('arr2: ' + arr2);
	  var f1 = function() {alert(111);};
	  var f2 = function() {alert(222);};
	  var arr5 = [0, false, null, '', f1, {A1: 'A1', A2: {A21: 'A21'}}, {B1: {B11: 'B11'}, B2: 'B2'}, {C1 : {C11 : {C111: 'C111'}}}];
	  var arr6 = arr5;              // 同一对象
	  var arr7 = arr5.concat();     // 另一数组, 已有元素同一对象
	  var arr8 = arr5.clone(false); // 另一数组, 浅克隆
	  var arr9 = arr5.clone();      // 另一数组, 深克隆
	  trace('BEFORE:');
	  trace('arr5: ' + $.toJSON(arr5));
	  trace('arr6: ' + $.toJSON(arr6));
	  trace('arr7: ' + $.toJSON(arr7));
	  trace('arr8: ' + $.toJSON(arr8));
	  trace('arr9: ' + $.toJSON(arr9));
	  arr5[0] = 1;
	  arr5[1] = true;
	  arr5[2] = undefined;
	  arr5[3] = 'Oracle';
	  arr5[4] = f2;
	  arr5[5].A1 = 'X1';
	  arr5[6].B1.B11 = 'Y11';
	  arr5[6].B1.Z12 = 'Z12';
	  arr5[7].C1.C11.C111 = 'S111';
	  arr5[8] = {D1: 'D1'};
	  trace('AFTER:');
	  trace('arr5: ' + $.toJSON(arr5));
	  trace('arr6: ' + $.toJSON(arr6));
	  trace('arr7: ' + $.toJSON(arr7));
	  trace('arr8: ' + $.toJSON(arr8));
	  trace('arr9: ' + $.toJSON(arr9));
	}

	// Object: clone
	function test06()
	{
	  var obj1 = {name: 'Harry Sun', gender: 'Male', son: {name: 'Authur', gender: 'Boy'}};
	  var obj2 = obj1;                     // 同一对象
	  var obj3 = cloneObject(obj1, false); // 浅克隆
	  var obj4 = cloneObject(obj1);        // 深克隆
	  trace('====== test06 ======');
	  trace('BEFORE:');
	  trace('obj1: ' + $.toJSON(obj1));
	  trace('obj2: ' + $.toJSON(obj2));
	  trace('obj3: ' + $.toJSON(obj3));
	  trace('obj4: ' + $.toJSON(obj4));
	  obj1.age = 36;
	  obj1.son.age = 7;
	  trace('AFTER:');
	  trace('obj1: ' + $.toJSON(obj1));
	  trace('obj2: ' + $.toJSON(obj2));
	  trace('obj3: ' + $.toJSON(obj3));
	  trace('obj4: ' + $.toJSON(obj4));
	}

	// jQuery EasyUI.messager
	function test07()
	{
	  trace('====== test07 ======');
	  var f1 = function(r) {
	    if (r) trace("YES");
	    else trace("NO");
	  };
	  var f2 = function(r) {
	    trace("typed: " + r);
	  }
	  $.messager.confirm(APP_TITLE, 'Hello World 1', f1);
	  $.messager.prompt(APP_TITLE, 'Hello World 2', f2);
	}
	
	// 可以使用for (var x in any) 来访问数组和对象!
	function test08()
	{
	  var a = {name: "AAA"};
	  var b = {name: "BBB"};
	  var c = {name: "CCC"};
	  var obj = {a:a,b:b,c:c};
	  var arr = [a,b,c];
	  trace('====== test08 ======');
	  for (var x in obj) trace(x + ':' + $.toJSON(obj[x]));
	  for (var y in arr) trace(y + ':' + $.toJSON(arr[y]));
	  for (var i = 0; i < arr.length; i++) trace(i + ":" + $.toJSON(arr[i]));
	}

	// String::empty
	function test09(event)
	{
	  if (event && event.data) {
	    $.messager.alert(APP_TITLE, event.data.foo);
	    return;
	  }
	  trace('====== test09 ======');
	  var arr = ['', '   ', '	', '  a	'];
	  for (var x in arr) {
	    if (typeof(arr[x]) == 'string') trace(arr[x] + ': ' + arr[x].empty());
	  }
	}

	// String::repeat & width
	function test10()
	{
	  trace('====== test10 ======');
		trace('abc, 10: ' + 'abc'.repeat(10));
		var s = 'a\nbc\ndef\n\ghij\nklmnoz\npqrs\ntuv\nwx\ny\n';
		trace('s :' + s + ', width: ' + s.width());
		// trace($('#console'));
		var f = function() {$.messager.alert(APP_TITLE, 'Hello World!');};
		trace('f: ' + typeof(f) + ', ' + getClass(f));
		$('#console').bind('keydown', {foo:'bar'}, test09);
	}

	// Array::prev & next
	function test11()
	{
	  trace('====== test11 ======');
	  var o = {a:'AAA'};
	  var d = new Date(2012,0,1,0,0,0,0);
	  var f = function() {$.messager.alert(APP_TITLE, 123);};
	  var arrs = [[6], [null, undefined, 6, 'Hello', d, o, f, true]];
	  var ts = [6, 8, 'Hello', o];
	  for (var i = 0; i < arrs.length; i++) {
	    trace(arrs[i]);
	  	trace('  next(): ' + arrs[i].next());
	  	trace('  prev(): ' + arrs[i].prev());
	    for (var j = 0; j < ts.length; j++) {
	    	trace('  next(' + ts[j] + '): ' + arrs[i].next(ts[j]));
	    	trace('  prev(' + ts[j] + '): ' + arrs[i].prev(ts[j]));
	    }
	  }
	}

	// Misc: prime number
	function test12()
	{
	  // find prime numbers between 1 and 100
	  var re = /^1?$|^(11+)\1+$/g;
	  var s = '';
	  for (var i = 1; i <= 100; i++) {
	    if (!'1'.repeat(i).match(re)) s = s + i + ',';
	  }
	  trace('====== test12 ======');
	  trace(s);
	  trace(${APP_DATA});
	}
	
	function test15()
	{
	  trace('====== test15 ======');
	  var obj = {'#name': 'Harry Sun', '#age': 36, '#hello': function() {$.messager.alert(APP_TITLE, 'Hello: ' + this['#name']);}};
	  obj['#hello']();
	  $.messager.alert(APP_TITLE, '#console.val=' + $('#console').val() + '; #t1.val=' + $('#t1').val());
	}

	// Date::format
	function test16()
	{
	  trace('====== test16 ======');
	  var now = new Date(1319328606000);
	  $.messager.alert(APP_TITLE, now.format('Y-m-d H:i:s'));
	}

	// jQuery::extend
	function test17()
	{
	  trace('====== test17 ======');
	  var a = {name:'A', value:1};
	  var b = {name:'B', text:'bbb'};
	  var c = $.extend(cloneObject(a), b);
	  trace([a,b,c]);
	}

	// Array::concat
	function test18()
	{
	  var arr1 = ['A', 'B', 'C'];
	  var arr2 = ['X', 'Y', 'Z'];
	  var arr3 = arr1.concat(arr2);
	  var arr4 = arr2.concat(arr1);
	  trace('====== test18 ======');
	  trace('arr1: ' + $.toJSON(arr1));
	  trace('arr2: ' + $.toJSON(arr2));
	  trace('arr3: ' + $.toJSON(arr3));
	  trace('arr4: ' + $.toJSON(arr4));
	}

	// Dynamic CSS href
	function test19()
	{
	  var name = '';
	  // 支持3种分辨率: 1600*900, 1280*720, 1024*768
	  if (screen.width < 1024) name = '';
	  else if (screen.width < 1280) name = '1024';
	  else if (screen.width < 1600) name = '1280';
	  else name = '1600';
	  var href = APP_CONTEXT_PATH + '/css/main_' + name + '.css'; // 使用此种方法动态改变css不能加?xx=xx
	  $('#info19').text(screen.width + '*' + screen.height + ':' + href);
	  $('#css_main').attr('href', href);
	}

	// Dynamic CSS style
	var g_color = 1;
	function test20()
	{
	  g_color = !g_color;
	  // in chrome, APP_CSS['.test'].style.color = 'rgb(255, 0, 255)'
	  APP_CSS['.test'].style.color = (g_color ? '#00FF00' : '#FF00FF'); 
	}

	// JSON
	function test21()
	{
	  var data = ${APP_DATA};
	  trace('====== test21 ======');
	  trace(data);
	}

	function _onTest(mi) {
	  if (mi.hasOwnProperty('name')) eval(mi.name + '()');
	  else trace('NO method: ' + mi.text);
	}

	APP_EVENTS.on('test', _onTest);
})(jQuery);

$(function() {
  $.messager.alert(APP_TITLE, APP_NAME, 'error');
  $('#console').itip('控制台日志');
  $('#console').data('name', 'Harry');
});

</script>
</body>
</html>