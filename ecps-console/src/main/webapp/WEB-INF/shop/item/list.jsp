<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/ecps/console/common/taglibs.jsp"%>
<head>
<title>商品录入及上下架管理_商品管理</title>
<meta name="heading" content="商品录入及上下架管理" />
<meta name="menu" content="ItemMgmtMenu" />
<script type="text/javascript"
	src="<c:url value='/${system}/res/js/jquery.form.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/${system}/res/js/jquery.tablesorter.js'/>"></script>
<script language="javascript" type="text/javascript">
	$(function() {
		//获得上下架的状态值,只要是在页面上获得的值都是String 类型
		var showStatus = $("#showStatus").val();
		//把字符串类型的数据转换成数值类型
		showStatus = parseInt(showStatus);
		if(showStatus == 1){
			//给未上架的a链接加选中样式
			$("#label4").attr("class","here");
		}else if(showStatus == 0){
			//给已上架的a链接加选中样式
			$("#label5").attr("class","here");
		}else{
			$("#label6").attr("class","here");
		}
		
		
		//获得当前页码
		var pageNo = parseInt($("#currentPageNo").val());
		//获得总页数
		var totalPage = parseInt($("#totalPage").val());
		//总页数只有一页
		if(pageNo == 1 && totalPage == 1){
			$("#firstPage").hide();
			$("#next").hide();
			$("#previous").hide();
			$("#lastPage").hide();
		}else if(pageNo == 1 && totalPage > 1){//展示下一页和尾页
			$("#firstPage").hide();
			$("#next").show();
			$("#previous").hide();
			$("#lastPage").show();
		}else if(pageNo > 1 && totalPage > pageNo){//四个按钮都展示
			$("#firstPage").show();
			$("#next").show();
			$("#previous").show();
			$("#lastPage").show();
		}else if(pageNo == totalPage && pageNo > 1){//只展示上一页和首页
			$("#firstPage").show();
			$("#next").hide();
			$("#previous").show();
			$("#lastPage").hide();
		}
		
		$("#next").click(function(){
			//把pageNo隐藏域加1
			$("#pageNo").val(++pageNo);
			//表单提交
			$("#form1").submit();
		});
		$("#previous").click(function(){
			//把pageNo隐藏域加1
			$("#pageNo").val(--pageNo);
			//表单提交
			$("#form1").submit();
		});
		$("#firstPage").click(function(){
			//把pageNo隐藏域加1
			$("#pageNo").val(1);
			//表单提交
			$("#form1").submit();
		});
		$("#lastPage").click(function(){
			//把pageNo隐藏域加1
			$("#pageNo").val(totalPage);
			//表单提交
			$("#form1").submit();
		});
		$("#selectPage").change(function(){
			//把pageNo隐藏域加1
			$("#pageNo").val($(this).val());
			//表单提交
			$("#form1").submit();
		});
		$("#selectPage").val(pageNo);
		
		$("#addItemNoteConfirm").click(function(){
			//获得上下架意见
			var notes = $("#itemNote").val();
			//给表单中的上下架意见文本域赋值
			$("#notes").val(notes);
			//获得到表单
			$("#showForm").submit();
		});
	})
	
	function isShow(itemId,showStatus){
		$("#itemNote").val("");
		//给表单中的itemId和showStatus赋值
		$("#itemId").val(itemId);
		$("#myShowStatus").val(showStatus);
		tipShow("#addItemNote");
	}
	
	function publish(itemId){
		tipShow("#refundLoadDiv");
		var option = {
				url:"${path}/item/publish.do",
				type:"post",
				dataType:"text",
				data:{
					itemId:itemId
				},
				success:function(responseText){
					if(responseText == "success"){
						alert("发布成功");
					}else{
						alert("发布失败");
					}
					tipHide("#refundLoadDiv");
				},
				error:function(){
					alert("系统错误");
				}
		}
		$.ajax(option);
	}
</script>
</head>
<body id="main">
	<form id="showForm" action="${path}/item/showItem.do" method="post">
		<input type="hidden" name="itemId" id="itemId" />
		<input type="hidden" name="showStatus" id="myShowStatus" />
		<input type="hidden" name="notes" id="notes" >
	</form>
	<div class="frameL">
		<div class="menu icon">
			<jsp:include page="/${system}/common/itemmenu.jsp" />
		</div>
	</div>

	<div class="frameR">
		<div class="content">
			<div class="loc icon">
				<samp class="t12"></samp>
				当前位置：商品管理&nbsp;&raquo;&nbsp;<span class="gray" title="商品录入及上下架">商品录入及上下架</span>
			</div>
			<h2 class="h2_ch">
				<span id="tabs" class="l"> 
					<a id="label6" href="${path}/item/listItem.do?auditStatus=1" title="全部实体商品" class="nor">全部</a> 
					<a id="label4" href="${path}/item/listItem.do?showStatus=1&auditStatus=1" title="未上架实体商品" class="nor">未上架</a> 
					<a id="label5" href="${path}/item/listItem.do?showStatus=0&auditStatus=1" title="已上架实体商品" class="nor">已上架</a>
				</span>
			</h2>

			<form id="form1" name="form1" action="${path}/item/listItem.do" method="post">
				<input type="hidden" id="showStatus" name="showStatus" value="${qc.showStatus }" />
				<div class="sch">
					<input type="hidden" id="userSearch" name="userSearch" />
					<p>
						搜索： 
						<select id="brandId" name="brandId">
							<option value="">全部品牌</option>
							<c:forEach items="${bList }" var="brand">
								<option value="${brand.brandId }" <c:if test="${brand.brandId == qc.brandId }">selected</c:if>>${brand.brandName }</option>
							</c:forEach>
						</select> 
						<select id="auditStatus" name="auditStatus">
							<option value="" selected>全部审核状态</option>
							<option value="0" <c:if test="${qc.auditStatus == 0 }">selected</c:if>>待审核</option>
							<option value="1" <c:if test="${qc.auditStatus == 1 }">selected</c:if>>审核通过</option>
							<option value="2" <c:if test="${qc.auditStatus == 2 }">selected</c:if>>审核不通过</option>
						</select> 
						<input type="text" id="searchText" value="${qc.itemName }" name="itemName" title="请输入商品名称" class="text20 medium gray" /> 
						<input type="submit" id="goSearch" class="hand btn60x20" value="查询" />
					</p>
				</div>

				<div class="page_c">
					<span class="l"> </span>
				 	<span class="r inb_a"> 
				 		<a href="${path}/item/toAddItem.do" class="btn80x20" title="添加商品">添加商品</a>
					</span>
				</div>

				<table cellspacing="0" summary="" class="tab" id="myTable">
					<thead>
						<tr>
							<th>商品编号</th>
							<th class="wp">商品名称</th>
							<th>图片</th>
							<th>新品</th>
							<th>推荐</th>
							<th>特价</th>
							<th>上下架</th>
							<th>审核状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list }" var="item">
						<tr>
							<td>${item.itemNo }</td>
							<td>${item.itemName }</td>
							<td>
								<img alt="" src="${file_path }${item.imgs }" width="50" height="50">
							</td>
							<td>
								<c:if test="${item.isNew == 1 }"><span class="is"></span></c:if>
								<c:if test="${item.isNew == 0 }"><span class="not"></span></c:if>
							</td>
							<td>
								<c:if test="${item.isGood == 1 }"><span class="is"></span></c:if>
								<c:if test="${item.isGood == 0 }"><span class="not"></span></c:if>
							</td>
							<td>
								<c:if test="${item.isHot == 1 }"><span class="is"></span></c:if>
								<c:if test="${item.isHot == 0 }"><span class="not"></span></c:if>
							</td>
							<td>
								<c:if test="${item.showStatus == 1 }"><span class="not"></span></c:if>
								<c:if test="${item.showStatus == 0 }"><span class="is"></span></c:if>
							</td>
							<td>
								<c:if test="${item.auditStatus == 0 }">待审核</c:if>
								<c:if test="${item.auditStatus == 1 }">通过</c:if>
								<c:if test="${item.auditStatus == 2 }">不通过</c:if>
							</td>
							<td>
								<a href="/ecps-console/shop/item/viewItem.jsp" title="查看">查看</a>
								<c:if test="${item.showStatus == 1 }">
									<a href = "javascript:void(0);" onclick="">编辑</a>
									<a href = "javascript:void(0);" onclick="singleDel('2834')">删除</a>
									<a href = "javascript:void(0);" onclick="isShow(${item.itemId},0)">上架</a>
								</c:if>
								<c:if test="${item.showStatus == 0 }">
									<a href = "javascript:void(0);" onclick="isShow(${item.itemId},1)">下架</a>
									<a href = "javascript:void(0);" onclick="publish(${item.itemId})">发布 </a>
								</c:if>
							</td>
						</tr>
						</c:forEach>
					</tbody>
					<tr>
						<td colspan="13" align="right">
							选择：
							<a href="javascript:void(0);" title="全选" onclick="checkAllIds();">全选</a>
							<samp>-</samp> 
							<a href="javascript:void(0);" title="取消" onclick="uncheckAllIds();">取消</a>
						</td>
					</tr>
				</table>
				<div class="page_c"> 
					<span class="l inb_a"></span>
					<span class="r page">
						<!-- 用于把变化后的页面传递给后台（controller） -->
						<input type="hidden" id="pageNo" name="pageNo"/>
						<input type="hidden" value="${page.totalCount }" id="totalCount" name="totalCount"/>
						<input type="hidden" value="${page.pageNo }" id="currentPageNo" name="currentPageNo"/>
						<input type="hidden" value="${page.totalPage }" id="totalPage" name="totalPage"/>
						共<var id="pagePiece" class="orange">${page.totalCount }</var>条
						<var id="pageTotal">${page.pageNo }/${page.totalPage }</var>
						<a href="javascript:void(0);" id="firstPage" class="hidden">首页</a>
						<a href="javascript:void(0);" id="previous" class="hidden" title="上一页">上一页</a>
						<a href="javascript:void(0);" id="next" class="hidden" title="下一页">下一页</a>
						<select id="selectPage">
							<c:forEach begin="1" end="${page.totalPage }" var="myPage">
								<option value="${myPage }">第${myPage }页</option>
							</c:forEach>
						</select>
						<a href="javascript:void(0);" id="lastPage" class="hidden" >尾页</a>
					</span>
				</div>
			</form>
		</div>
	</div>
</body>