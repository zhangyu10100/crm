<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){

		//为创建按钮绑定事件，添加操作的模态窗口
		$("#addBtn").click(function (){

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			/*操作模态窗口的方式
			需要操作的模态窗口的jQuery对象，调用model方法，为该方法传递参数show:打开模态窗口  hide：关闭模态窗口*/
			//$("#createActivityModal").modal("show");
			//走后台，目的是为了取得用户信息列表，为所有者下拉框赋值
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success : function (data){
					var html = "<option></option>";
					$.each(data,function (i,n){
						html += "<option value='"+n.id+"'>"+n.name+"</option>";
					})
					$("#create-owner").html(html);

					var id = "${user.id}";
					$("#create-owner").val(id);

					$("#createActivityModal").modal("show");

				}
			})

		})

		//为保存按钮绑定事件，执行添加操作
		$("#saveBtn").click(function (){
			$.ajax({
				url:"workbench/activity/save.do",
				data:{

					"name" : $.trim($("#create-name").val()),
					"owner" : $.trim($("#create-owner").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"startDate" : $.trim($("#create-startDate").val()),
				},
				type:"post",
				dataType:"json",
				success:function (data){
					/*data "
					* {"success":true/false}
					* */
					if (data.success){
						//pageList(1,2);


						/*$("#activityPage").bs_pagination('getOption', 'currentPage')
						* 表示操作后停留在当前页
						*
						* $("#activityPage").bs_pagination('getOption', 'rowsPerPage'
						* 表示操作后维持已经设置好的每页展现的记录数
						*
						* 这两个参数不需要我们进行任何的修改
						* */
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//刷新市场活动信息列表（局部刷新）

						//清空模态窗口的数据
						$("#activityAddForm")[0].reset();

						//关闭添加操作的模态窗口

						$("#createActivityModal").modal("hide");
					}else {
						alert("添加市场活动失败");
					}
				}
			})
		})
		//页面加载完毕后触发一个方法
		pageList(1,2);

		//为查询按钮绑定事件触发pageList方法
		$("#searchBtn").click(function (){

			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,2);
		})
		//为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function (){
			$("input[name=xz]").prop("checked",this.checked);
		})


		//动态生成的元素，不能直接使用普通绑定事件的形式来进行操作的
		//动态生成的元素，我们要以on方法的形式来触发事件
		/*
		* 语法
		* 	$(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素的jQuery对象，回调函数)
		* */
		$("#activityBody").on("click",$("input[name=xz]"),function (){
			$("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);
		})

		//为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function (){

			//找到复选框中所有勾中的复选框的jQuery对象
			var $xz = $("input[name=xz]:checked");
			if ($xz.length == 0){
				alert("请删除需要删除的记录")
			}else{
				//workbench/activity/delete.do?id=xxx&id=xxx
				if (confirm("确定删除所选中的记录吗？")){
					var param = "";

					//将$xz中的每一个对象遍历出来，取其value值，就相当于取得了需要删除的记录的id
					for(var i = 0;i<$xz.length;i++){
						//$xz[i].value();
						param += "id=" + $($xz[i]).val();
						if (i<$xz.length-1){
							param += "&";
						}

					}
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function (data){
							if (data.success){
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								alert("删除市场活动失败");
							}
						}
					})

				}

			}

		})

		//为修改按钮绑定事件，打开修改操作的模态窗口
		$("#editBtn").click(function (){
			var $xz = $("input[name=xz]:checked");
			if ($xz.length == 0){
				alert("请选择需要修改的记录");
			}else if ($xz.length > 1){
				alert("一次只能修改一条记录");
			}else {
				var id = $xz.val();
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						"id":id
					},
					type:"get",
					dataType:"json",
					success:function (data){
						var html = "<option></option>";
						$.each(data.uList,function (i,n){
							html += "<option value='"+n.id+"'>"+n.name+"</option>";

						})

						$("#edit-owner").html(html);

						//处理单条activity
						$("#edit-name").val(data.a.name);
						$("#edit-owner").val(data.a.owner);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);
						$("#edit-id").val(data.a.id);

						//打开修改操作的模态窗口
						$("#editActivityModal").modal("show");

					}
				})
			}
		})

		$("#updateBtn").click(function (){
			/* var $xz = $("input[name=xz]:checked") ;
			var id = $xz.val();*/
			$.ajax({
				url:"workbench/activity/update.do",
				data:{

					"name" : $.trim($("#edit-name").val()),
					"owner" : $.trim($("#edit-owner").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-description").val()),
					"endDate" : $.trim($("#edit-endDate").val()),
					"startDate" : $.trim($("#edit-startDate").val()),
					"id" : $.trim($("#edit-id").val())
				},
				type:"post",
				dataType:"json",
				success:function (data){
					/*data "
					* {"success":true/false}
					* */
					if (data.success){
						//刷新市场活动信息列表（局部刷新）
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//关闭添加操作的模态窗口

						$("#editActivityModal").modal("hide");
					}else {
						alert("修改市场活动失败");
					}
				}
			})
		})
	});

	/*
	* 对于所有的关系型数据库，做前端分页相关操作的基础组件
	* 就是pageNo和pageSize
	* pageNo:页码
	* pageSize:每页展示的记录数
	*
	* pageList方法：就是发出Ajax请求到后台，从后台取得最新的市场活动信息列表数据
	* 通过响应回来的数据，局部刷新市场活动信息列表
	*
	*
	* */
	function pageList(pageNo,pageSize){

		//将全选的复选框的勾干掉
		$("#qx").prop("checked",false);


		//查询钱，将隐藏域中保存的信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val())
			},
			type:"get",
			dataType:"json",
			success:function (data){
				var html = "";
				$.each(data.dataList,function (i,n){
				html += '<tr class="active">';
				html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
				html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activitydetail.jsp\';">'+n.name+'</a></td>';
				html += '<td>'+n.owner+'</td>';
				html += '<td>'+n.startDate+'</td>';
				html += '<td>'+n.endDate+'</td>';
				html += '</tr>';
				})

				$("#activityBody").html(html);

				//计算总页数
				var totalPages = data.total % pageSize == 0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				//数据处理完毕后，结合分页插件对前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数在点击分页组件的时候触发
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}
		})
	}

</script>
</head>
<body>
	<input type="hidden" id="hidden-name" />
	<input type="hidden" id="hidden-owner" />
	<input type="hidden" id="hidden-startDate" />
	<input type="hidden" id="hidden-endDate" />

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: #ff0000;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" >
							</div>
							<label for="create-endTime" class="col-sm-2 control-label" >结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" >
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<%--
					data-dismiss="modal":表示关闭模态窗口

					--%>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id"/>
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<%--关于textarea
									一定要以标签对来呈现，正常状态下必须紧紧挨着
									textarea虽然是以标签对的形式来呈现的，但是他也是属于表单元的范畴
									我们所有的对于textarea的取值和赋值操作应该统一使用val（）方法而并不是HTML方法--%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<%--data-toggle="modal":表示触发该按钮将打开一个模态窗口
					data-target="#createActivityModal"表示打开哪个模态窗口，通过#id的形式找到该窗口
					现在是通过属性和属性值的方式写在button元素中用来打开模态窗口
					但是这样做是有问题的。问题在于没有办法对按钮的功能进行扩充
					所以未来的实际项目开发对于触发模态窗口的操作一定不要写死在元素中‘
					应该自己写js代码来操作--%>
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activitydetail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage">
				</div>

			</div>

		</div>
		
	</div>
</body>
</html>