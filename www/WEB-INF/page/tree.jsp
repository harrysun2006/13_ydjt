<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Tree Context Menu - jQuery EasyUI Demo</title>
	<link rel="stylesheet" type="text/css" href="${APP_CONTEXT_PATH}/js/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${APP_CONTEXT_PATH}/js/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${APP_CONTEXT_PATH}/css/demo.css">
	<script type="text/javascript" charset="utf-8" src="${APP_CONTEXT_PATH}/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="${APP_CONTEXT_PATH}/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="${APP_CONTEXT_PATH}/js/jquery.json-2.3.min.js"></script>
</head>
<body>
	<h2>Tree Context Menu</h2>
	<div class="demo-info">
		<div class="demo-tip icon-tip"></div>
		<div>Right click on a node to display context menu.</div>
	</div>
	<div style="margin:10px 0;">
	</div>
	<ul id="tt" class="easyui-tree tree" data-options="
			animate: true,
			onContextMenu: function(e,node){
				e.preventDefault();
				$(this).tree('select',node.target);
				$('#mm').menu('show',{
					left: e.pageX,
					top: e.pageY
				});
			}
		"></ul>
	<div id="mm" class="easyui-menu" style="width:120px;">
		<div onclick="append()" data-options="iconCls:'icon-add'">Append</div>
		<div onclick="remove()" data-options="iconCls:'icon-remove'">Remove</div>
		<div class="menu-sep"></div>
		<div onclick="expand()">Expand</div>
		<div onclick="collapse()">Collapse</div>
	</div>
	<script type="text/javascript">
  var json = $.toJSON({category:'questions'});
  console.log(json);
  $.ajax({
    type: 'POST',
    contentType: 'application/json',
    url: '/folder/tree',
    data: json,
    dataType: 'json',
    success: function(result) {
      console.log(result.tree);
      $('#tt').tree('loadData', result.tree);
    }
  });
		function append(){
			var t = $('#tt');
			var node = t.tree('getSelected');
			t.tree('append', {
				parent: (node?node.target:null),
				data: [{
					text: 'new item1'
				},{
					text: 'new item2'
				}]
			});
		}
		function remove(){
			var node = $('#tt').tree('getSelected');
			$('#tt').tree('remove', node.target);
		}
		function collapse(){
			var node = $('#tt').tree('getSelected');
			$('#tt').tree('collapse',node.target);
		}
		function expand(){
			var node = $('#tt').tree('getSelected');
			$('#tt').tree('expand',node.target);
		}
	</script>
</body>
</html>