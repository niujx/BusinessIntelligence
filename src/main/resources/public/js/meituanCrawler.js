(function($){
	
	var address = "http://123.206.22.165:9578/bi/mt/";
	/**************************美团外卖*******************************/
	
	//点击报表下载启动按钮时
	$(".meituan .mt_report_forms").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "bizDataReport",//请求路径
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
				alert("启动成功");
//				$(this).parents("tr").find(".getTime").text();//text(括号里放返回的日期)
			},
			error:function(e){
				//请求失败时的函数
				alert("请求失败");
				console.log("请求失败");//控制台输出请求失败
			}
		})
	})
	
	
	
	//点击营业统计启动按钮时
	$(".meituan .mt_crawler_sale").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "businessStatistics",//请求路径
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
				alert("启动成功");
//				$(this).parents("tr").find(".getTime").text();//text(括号里放返回的日期)
			},
			error:function(e){
				//请求失败时的函数
				alert("请求失败");
				console.log("请求失败");//控制台输出请求失败
			}
		})
	})
	
	
	
	
	//点击流量分析启动按钮时
	$(".meituan .mt_crawler_flow").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "flow",//请求路径
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
				alert("启动成功");

			},
			error:function(e){
				//请求失败时的函数
				alert("请求失败");
				console.log("请求失败");//控制台输出请求失败
			}
		})
	})
	
	
	
	
	
	//点击热销商品启动按钮时
	$(".meituan .mt_goods_sale").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "hotSales",//请求路径
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
				alert("启动成功");

			},
			error:function(e){
				//请求失败时的函数
				alert("请求失败");
				console.log("请求失败");//控制台输出请求失败
			}
		})
	})
	
	
	//点击订单对账启动按钮时
	$(".meituan .mt_order_checking").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "bill",//请求路径
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
				alert("启动成功");

			},
			error:function(e){
				//请求失败时的函数
				alert("请求失败");
				console.log("请求失败");//控制台输出请求失败
			}
		})
	})
	
	
	
	//点击指定门店点评内容启动按钮时
	$(".meituan .mt_crawler_evaluate").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "comment",//请求路径
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
				alert("启动成功");

			},
			error:function(e){
				//请求失败时的函数
				alert("请求失败");
				console.log("请求失败");//控制台输出请求失败
			}
		})
	})
	
	
	
	
	//点击营销活动启动按钮时
	$(".meituan .mt_sale_activity").on('click',function(){
		//获取当前抓取时间及用户名
		var startDateTime = $(this).parents("tr").find(".startDateTime").val();
		var endDateTime = $(this).parents("tr").find(".endDateTime").val();
		var merchantUserName =  $(this).parents("tr").find(".merchantUserName").val();
		//发送请求
		$.ajax({
			url:address + "acts",//请求路径
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
				alert("启动成功");

			},
			error:function(e){
				//请求失败时的函数
				alert("请求失败");
				console.log("请求失败");//控制台输出请求失败
			}
		})
	})
	
	
	
	
	
	
})(jQuery);
