$(function(){
	$.ajax({
		url:path+"/user/getUser.do",
		type:"post",
		dataType:"text",
		success:function(responseText){
			//把字符串转换成json对象
			var jsonObj = $.parseJSON(responseText);
			if(jsonObj.user != null){
				$("#loginAlertIs").html(jsonObj.user.username);				
			}
		},
		error:function(){
			alert("系统错误");
		}
	})
})