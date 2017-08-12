(function($){
	
		var address = "http://localhost:9578/bi/";
	
window.onload=function(){

		//发送请求
		$.ajax({
			url:address + "getAllStatus",//请求路径
			type:"get",//请求方式 get和post
			dataType:"json",//返回数据的格式
			data:{
			},
			success:function(e){
				//e 为请求成功后返回的数据
				
				$(".elmstate").each(function(i,val){
					$(val).html(e.eleme[i].status);
				})
				$(".mtstate").each(function(i,val){
					$(val).html(e.mt[i].status);
				})
				$(".bdstate").each(function(i,val){
					$(val).html(e.bd[i].status);
				})
				
				
				
				$(this).parents("tr").find(".getTime").text();//text(括号里放返回的日期)
			},
			error:function(e){
				//请求失败时的函数
				alert(e);
				console.log("请求失败");//控制台输出请求失败
			}
		})
	
	
}
})(jQuery);
