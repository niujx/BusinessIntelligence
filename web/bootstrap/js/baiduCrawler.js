(function($){
	
	var address = "http://localhost:9578/bi/";
	var add = "http://localhost:8888/platform/demo/get"
	/**************************百度外卖*******************************/
	//点击开抓按钮时
	$(".baidu .orderBegin").on('click',function(){
		//获取当前抓取时间
		var startTime = $(this).parents("tr").find(".startDateTime").val();
		var endTime = $(this).parents("tr").find(".endDateTime").val();
		//发送请求
		$.ajax({
			url:add,//请求路径
			type:"get",//请求方式 get和post
			dataType:"json",//返回数据的格式
			data:{
				//给后台发送数据	抓取时间	beginDate是要传给后台的字段名 可换 ,curTime是抓取时间
				startDate: startTime ,
				endDate: endTime
			},
			success:function(e){
				//e 为请求成功后返回的数据
				//console.log(e)
//				$(this).parents("tr").find(".getTime").text();//text(括号里放返回的日期)
				alert("请求成功，请耐心等待数据抓取，不要重复抓取");
			},
			error:function(e){
				//请求失败时的函数
				alert(e);
				console.log("请求失败，请稍后重新尝试");//控制台输出请求失败
			}
		})
	})
	
	//点击下载按钮时
	$(".baidu .download").on("click",function(){
		//发送请求
		$.ajax({
			url:address+"getTime",//请求路径
			type:"get",//请求方式 get和post
			dataType:"json",//返回数据的格式
			success:function(e){
				//e 为请求成功后返回的数据
				//console.log(e)
				$(this).parents("tr").find(".getTime").text(e);
			},
			error:function(){
				//请求失败时的函数
				console.log("请求失败");//控制台输出请求失败
			}
		})
	})
})(jQuery);
