<%@ include file="/ecps/console/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
<title>添加品牌_品牌管理_商品管理</title>
<meta name="heading" content="品牌管理" />
<meta name="tag" content="tagName" />
<script type="text/javascript"
	src="<c:url value='/${system}/res/js/jquery.form.js'/>"></script>
<script type="text/javascript">
	$(function(){
		$("#form111").submit(function(){
			var isSubmit = true;
			//获得到表单中所有的必填字段
			$(this).find("[reg2]").each(function(){
				//获得填入的值
				var val = $(this).val();
				//获得提示信息
				var tip = $(this).attr("tip");
				//获得正则表达式的字符串
				var regStr = $(this).attr("reg2");
				//创建正则表达式对象
				var reg = new RegExp(regStr);
				if(!reg.test($.trim(val))){
					//阻止表单提交
					isSubmit = false;
					//展示提示信息
					$(this).next("span").html("<font color='red'>"+tip+"</font>");
					//跳出foreach,使用return false
					return false;
				}else{
					//获得每一个合法的input的name
					var inputName = $(this).attr("name");
					if(inputName == "brandName"){
						//校验品牌名称的重复性
						var result = validBrandName(val);
						if(result == "no"){
							isSubmit = false;
							$(this).next("span").html("<font color='red'>品牌名称已经存在</font>");
							return false;
						}else{
							$(this).next("span").html("");
						}
					}else{
						$(this).next("span").html("");
					}
				}
			});
		
			$(this).find("[reg1]").each(function(){
				//获得填入的值
				var val = $(this).val();
				//获得提示信息
				var tip = $(this).attr("tip");
				//获得正则表达式的字符串
				var regStr = $(this).attr("reg1");
				//创建正则表达式对象
				var reg = new RegExp(regStr);
				if(val != null && $.trim(val) != "" && !reg.test($.trim(val))){
					//阻止表单提交
					isSubmit = false;
					//展示提示信息
					$(this).next("span").html("<font color='red'>"+tip+"</font>");
					//跳出foreach,使用return false
					return false;
				}else{
					$(this).next("span").html("");
				}
			});
			
			if(isSubmit){
				tipShow("#refundLoadDiv");
			}
			//如果返回时FALSE就阻止表单提交，TRUE可以提交
			return isSubmit;
		})
		
		
		$("form111").find("[reg2]").blur(function(){
			//获得填入的值
			var val = $(this).val();
			//获得提示信息
			var tip = $(this).attr("tip");
			//获得正则表达式的字符串
			var regStr = $(this).attr("reg2");
			//创建正则表达式对象
			var reg = new RegExp(regStr);
			if(!reg.test($.trim(val))){
				//展示提示信息
				$(this).next("span").html("<font color='red'>"+tip+"</font>");
			}else{
				//获得每一个合法的input的name
				var inputName = $(this).attr("name");
				if(inputName == "brandName"){
					//校验品牌名称的重复性
					var result = validBrandName(val);
					if(result == "no"){
						$(this).next("span").html("<font color='red'>品牌名称已经存在</font>");
					}else{
						$(this).next("span").html("");
					}
				}else{
					$(this).next("span").html("");
				}
			}
		});
		
		$("form111").find("[reg1]").blur(function(){
			//获得填入的值
			var val = $(this).val();
			//获得提示信息
			var tip = $(this).attr("tip");
			//获得正则表达式的字符串
			var regStr = $(this).attr("reg1");
			//创建正则表达式对象
			var reg = new RegExp(regStr);
			if(val != null && $.trim(val) != "" && !reg.test($.trim(val))){
				//展示提示信息
				$(this).next("span").html("<font color='red'>"+tip+"</font>");
			}else{
				$(this).next("span").html("");
			}
		});
		
	})
	
	function validBrandName(brandName){
		var result = "yes";
		var option = {
			url:"${path}/item/validBrandName.do",
			type:"post",
			dateType:"text",
			data:{
				brandName:brandName
			},
			success:function(responseText){
				result = responseText;
			},
			error:function(){
				alert("系统错误");
			}
		}
		$.ajax(option);
		return result;
	}
	
	function submitUpload(){
		var option = {
			url:"${path}/upload/uploadPic.do",//如果不写url就是默认使用要提交的表单中的url，如果有url就是使用该url
			dataType:"text",
			success:function(responseText){
				//把json格式字符串转换成json对象
				var jsonObj = $.parseJSON(responseText);
				$("#imgsImgSrc").attr("src",jsonObj.realPath);
				$("#imgs").val(jsonObj.relativePath);
				$("#lastPath").val(jsonObje.realPath);
			},
			error:function(){
				alert("系统错误");
			}
		};
		//ajax方式提交表单，页面不会跳转（由jQuery-form提供）
		$("#form111").ajaxSubmit(option);
	}
</script>
</head>
<body id="main">
	<div class="frameL">
		<div class="menu icon">
			<jsp:include page="/${system}/common/itemmenu.jsp" />
		</div>
	</div>

	<div class="frameR">
		<div class="content">
			<c:url value="/${system}/item/brand/listBrand.do" var="backurl" />
			<div class="loc icon">
				<samp class="t12"></samp>
				当前位置：商品管理&nbsp;&raquo;&nbsp;<a
					href="<c:url value="/${system }/item/brand/listBrand.do"/>"
					title="品牌管理">品牌管理</a>&nbsp;&raquo;&nbsp;<span class="gray">添加品牌</span>
				<a href="<c:url value="/${system }/item/brand/listBrand.do"/>"
					title="返回品牌管理" class="inb btn80x20">返回品牌管理</a>
			</div>
			<form id="form111" name="form111" action="${path }/item/addBrand.do"
				method="post" enctype="multipart/form-data">
				<div class="edit set">
					<input type="hidden" id="lastPath" name="lastPath"/> 
					<p>
						<label><samp>*</samp>品牌名称：</label><input type="text"
							id="brandName" name="brandName" class="text state"
							reg2="^[a-zA-Z0-9\u4e00-\u9fa5]{1,20}$" tip="必须是中英文或数字字符，长度1-20" />
						<span></span>
					</p>
					<p>
						<label class="alg_t"><samp>*</samp>品牌LOGO：</label><img
							id='imgsImgSrc' src="" height="100" width="100" />
					</p>
					<p>
						<label></label><input type='file' size='27' id='imgsFile'
							name='imgsFile' class="file" onchange='submitUpload()' /><span
							id="submitImgTip" class="pos">请上传图片宽为120px，高为50px，大小不超过100K。</span>
						<input type='hidden' id='imgs' name='imgs' value='' reg2="^.+$"
							tip="亲！您忘记上传图片了。" /><span></span>
					</p>

					<p>
						<label>品牌网址：</label><input type="text" name="website"
							class="text state" maxlength="100" tip="请以http://开头"
							reg1="http:///*" /> <span class="pos">&nbsp;</span>
					</p>
					<p>
						<label class="alg_t">品牌描述：</label>
						<textarea rows="4" cols="45" name="brandDesc" class="are"
							reg1="^(.|\n){0,300}$" tip="任意字符，长度0-300"></textarea>
						<span class="pos">&nbsp;</span>
					</p>
					<p>
						<label>排序：</label><input type="text" id="brandSort"
							reg1="^[1-9][0-9]{0,2}$" tip="排序只能输入1-3位数的正整数" name="brandSort"
							class="text small" /> <span class="pos">&nbsp;</span>
					</p>
					<p>
						<label>&nbsp;</label><input type="submit" name="button1" d
							class="hand btn83x23" value="完成" /><input type="button"
							class="hand btn83x23b" id="reset1" value='取消'
							onclick="backList('${backurl}')" />
					</p>
				</div>
			</form>
			<div class="loc">&nbsp;</div>
		</div>
	</div>
</body>

