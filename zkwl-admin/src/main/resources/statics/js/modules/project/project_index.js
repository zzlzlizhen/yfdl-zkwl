$(function () {
    var pages
    var pageSize //每页条数
    var pageNum //数据返回第几页

    var serial_a=""
    var pro_name_a=""
    var select_a=""
    //总览
    $("#pandect").click(function () {
        window.open("../pandect/pandect.html")
    })


    //用户信息
    $.ajax({
        url:baseURL + 'sys/user/nameList',
        type:"get",
        data:{},
        success: function(res) {
            console.log("用户名")
            console.log(res)
            var html
            for( var i=0 ;i<=res.realName.length;i++){
                html+="<option>"+res.realName[i]+"</option>"
            }
            $("#realName,#select1").append(html)
        }
    })
    //搜索
    $("#proje_search").click(function(){
       var serial=$("#serial").val();
       var pro_name=$("#pro_name").val();
       var select=$("#realName option:selected").text()
       console.log("搜索")
       console.log(pageSize+pageNum+serial+pro_name+select)
        $("#div").html("")
       form(pageSize,pageNum,serial,pro_name,select)
    })

    //渲染表格
    form(10,1,"","","")

    function form(pageSizea,pagesa,serial,pro_name,select){
        $.ajax({
            url:baseURL + 'fun/project/queryProject',
            type:"POST",
            data:{
                "projectName":pro_name, //项目名称
                "projectCode": serial,//项目编号
                "userName": select,//用户
                "pageSize":pageSizea,
                "pageNum":pagesa
            },
            success: function(res){
                console.log("数据")
                console.log(res)
                pages=res.data.pages;
                pageSize=res.data.pageSize;
                pageNum=res.data.pageNum
                var html
                for(var i=0; i< res.data.list.length; i++){
                 html+=" <tr id="+res.data.list[i].projectId+">\n" +
                     "<td>"+res.data.list[i].projectCode+"</td>\n" +
                     "<td class='Na_me'>"+res.data.list[i].projectName+"</td>\n" +
                     "<td class='exclusiveUser'>"+res.data.list[i].exclusiveUser+"</td>\n" +
                     "<td class='sumCount'>"+res.data.list[i].sumCount+"</td>\n" +
                     "<td class='gatewayCount'>"+res.data.list[i].gatewayCount+"</td>\n" +
                     "<td class='faultCount'>"+res.data.list[i].faultCount+"</td>\n" +
                     "<td class='callPoliceCount'>"+res.data.list[i].callPoliceCount+"</td>\n" +
                     "<td>"+res.data.list[i].projectDesc+"</td>\n" +
                     "<td id="+res.data.list[i].projectId+">\n" +
                     "<a href=\"#\" class='modifier'><span class=\"glyphicon glyphicon-pencil\"></span></a>\n" +
                     "<a href=\"#\" class='particulars'><span class=\"glyphicon glyphicon-search\"></span></a>\n" +
                     "<a href=\"#\"><span class=\"glyphicon glyphicon-picture\"></span></a>\n" +
                     "<a href=\"#\" class='deleteq'><span class=\"glyphicon glyphicon-trash\"></span></a>\n" +
                     "</td>\n" +
                     "</tr>"
                }
                $("#div").append(html)

            //   删除
                $(".deleteq").click(function(){
                    var id=$(this).parent().attr('id');
                    $.ajax({
                        url:baseURL + 'fun/project/delete?projectIds='+id,
                        contentType: "application/json;charset=UTF-8",
                        type:"get",
                        data:{},
                        success: function(res) {
                          console.log("删除")
                          console.log(res)
                          window.location.reload()
                        }
                    })
                })
            // 编辑
                var proid
                $(".modifier").click(function(){
                    $(".shade_modifier,.shade_b_modifier").css("display","block");
                     proid=$(this).parent().attr('id');
                })
                $("#confirm_x").click(function(){
                    var pro_name_b= $(".pro_name_b").val()
                    var pro_s_b= $(".pro_s_b").val()
                    var select_b=$("#select1_b option:selected").text()
                    //正则  丸子
                    if(pro_name_b == ""||pro_s_b == ""||select_b == ""){
                        alert("輸入不能為空")
                    }else{
                        $.ajax({
                            url:baseURL + 'fun/project/update',
                            contentType: "application/json;charset=UTF-8",
                            type:"POST",
                            data:JSON.stringify({
                                "projectName":pro_name_b,
                                "projectId":proid,
                                "projectDesc":pro_s_b,
                                "exclusiveUser":select_b
                            }),
                            success: function(res){
                                window.location.reload()
                            }
                        });
                    }
                })
            // 分組
                $(".particulars").click(function(){
                    proid=$(this).parent().attr('id');
                    var Na_me=$(this).parent().siblings(".Na_me").html();
                    var exclusiveUser=$(this).parent().siblings(".exclusiveUser").html();
                    var sumCount=$(this).parent().siblings(".sumCount").html();
                    var gatewayCount=$(this).parent().siblings(".gatewayCount").html();
                    var faultCount=$(this).parent().siblings(".faultCount").html();
                    var callPoliceCount=$(this).parent().siblings(".callPoliceCount").html();

                    var searchUrl=encodeURI('../grouping/grouping.html?projectId='+proid+"&Na_me="+Na_me+"&exclusiveUser="+exclusiveUser+"&sumCount="+sumCount+"&gatewayCount="+gatewayCount+"&faultCount="+faultCount+"&callPoliceCount="+callPoliceCount)
                    location.href =searchUrl;
                })

            //  分頁
                $("#pagination3").pagination({
                    currentPage: pageNum,
                    totalPage: pages,
                    isShow: true,
                    count: 7,
                    homePageText: "首页",
                    endPageText: "尾页",
                    prevPageText: "上一页",
                    nextPageText: "下一页",
                    callback: function (current) {
                        //当前页数current
                     var  pagesb = current
                        $("#div").html("")
                        form(pageSize, pagesb)
                    }
                });


            }
        });

    }


// 新增//////////////////////////
   $("#proje_add").click(function(){
       $(".shade_project,.shade_b_project").css("display","block")
   })
    $(".shade_add_project").click(function(){
        $(".shade_project,.shade_b_project").css("display","none")
    })


    $("#project_confirm").click(function(){
       var pro_name= $(".pro_name").val()
       var pro_s= $(".pro_s").val()
       var select=$("#select1 option:selected").text()
        //正则  丸子
        if(pro_name ==""||pro_s==""||select=="" ) {
            alert("輸入不能為空")
        }else{
            $.ajax({
                url:baseURL + 'fun/project/add',
                contentType: "application/json;charset=UTF-8",
                type:"POST",
                data:JSON.stringify({
                    "projectName":pro_name,
                    "projectDesc":pro_s,
                    "exclusiveUser":select
                }),
                success: function(res){
                    window.location.reload()
                }
            });

        }


    })
    //编辑去弹窗/////////////////////////////////
     $(".shade_modifier_project").click(function(){
         $(".shade_modifier,.shade_b_modifier").css("display","none")
     })


})


