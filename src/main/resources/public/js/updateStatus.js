(function($){
	
		var address = "http://localhost:9578/bi/eleme/";
	
window.onload=function(){
	alert("aaa");
	
	
		//发送请求
		$.ajax({
			url:address + "getStatus",//请求路径
			type:"get",//请求方式 get和post
			dataType:"json",//返回数据的格式
			data:{
				//给后台发送数据	抓取时间	startTime等是要传给后台的字段名 可换 ,startDateTime是抓取开始时间
				startTime: startDateTime,
				endTime: endDateTime,
				userName: merchantUserName
			},
			success:function(e){
				//e 为请求成功后返回的数据
				console.log(e);
				alert(e);
//				$(this).parents("tr").find(".getTime").text();//text(括号里放返回的日期)
			},
			error:function(e){
				//请求失败时的函数
				alert(e);
				console.log("请求失败");//控制台输出请求失败
			}
		})
	
	
}
	
}