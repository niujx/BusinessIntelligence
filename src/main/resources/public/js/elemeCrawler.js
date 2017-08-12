(function($){
	
	var address = "http://localhost:9578/bi/eleme/";
	/**************************饿了么*******************************/
	//点击订单数据启动按钮时
	$(".eleme .crawlerOrder").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "crawlerOrder",//请求路径
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
				console.log("启动成功");
				alert(e);
			},
			error:function(e){
				//请求失败时的函数
				alert(e);
				console.log("请求失败");//控制台输出请求失败
			}
		})
	})
	
	//点击营业统计启动按钮时
	$(".eleme .crawlerSale").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "crawlerSale",//请求路径
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
	})
	
	
	//点击排名流量启动按钮时
	$(".eleme .crawlerFlow").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "crawlerFlow",//请求路径
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
	})
	
	

	//点击商品分析启动按钮时
	$(".eleme .crawlerCommodity").on('click',function(){
		//获取当前抓取时间及用户名
		
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "crawlerCommodity",//请求路径
			type:"get",//请求方式 get和post
			dataType:"json",//返回数据的格式
			data:{
				//给后台发送数据	抓取时间	startTime等是要传给后台的字段名 可换 ,startDateTime是抓取开始时间
				
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
	})
	
	
	

	//点击账单记录启动按钮时
	$(".eleme .crawlerBill").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "crawlerBill",//请求路径
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
	})
	



	//点击平台活动启动按钮时
	$(".eleme .crawlerActivity").on('click',function(){
		//获取当前抓取时间及用户名

		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "crawlerActivity",//请求路径
			type:"get",//请求方式 get和post
			dataType:"json",//返回数据的格式
			data:{
				//给后台发送数据	抓取时间	startTime等是要传给后台的字段名 可换 ,startDateTime是抓取开始时间
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
	})
	


	//点击顾客评价启动按钮时
	$(".eleme .crawlerEvaluate").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "crawlerEvaluate",//请求路径
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
	})
	

	
})(jQuery);
