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
            console.log("用户信息")
            console.log(res.user)
            var html=""
            for( var i=0 ;i< res.user.length;i++){
                html+="<option id="+res.user[i].userId+">"+res.user[i].realName+"</option>"
            }
            $("#realName,#select1,#select1_b").append(html)
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
                console.log("11111111")
                console.log(res)
                pages=res.data.pages;
                pageSize=res.data.pageSize;
                pageNum=res.data.pageNum
                var html=""
                for(var i=0; i< res.data.list.length; i++){
                    //项目描述
                    var projectDesc=res.data.list[i].projectDesc
                    if (projectDesc == null ) {
                        projectDesc = "";
                    } else  {
                        projectDesc = projectDesc;
                    }
                 html+=" <tr id="+res.data.list[i].projectId+">\n" +
                     "<td>"+res.data.list[i].projectCode+"</td>\n" +
                     "<td class='Na_me'>"+res.data.list[i].projectName+"</td>\n" +
                     "<td class='exclusiveUser'>"+res.data.list[i].exclusiveUserName+"</td>\n" +
                     "<td class='sumCount'>"+res.data.list[i].sumCount+"</td>\n" +
                     "<td class='gatewayCount'>"+res.data.list[i].gatewayCount+"</td>\n" +
                     "<td class='faultCount'>"+res.data.list[i].faultCount+"</td>\n" +
                     "<td class='callPoliceCount'>"+res.data.list[i].callPoliceCount+"</td>\n" +
                     "<td class='r_con_r'>"+projectDesc+"</td>\n" +
                     "<td id="+res.data.list[i].projectId+">\n" +
                     "<a href=\"#\" class='modifier'><img src='/remote-admin/statics/image/bianji.png' alt=''></span></a>\n" +
                     "<a href=\"#\" class='ma_p' id="+res.data.list[i].longitude+","+res.data.list[i].latitude+"><img src='/remote-admin/statics/image/ditu2.svg' alt='' style='width: 25px;height:25px;'></a>\n" +
                     "<a href=\"#\" class='deleteq'><img src='/remote-admin/statics/image/shanchu.png' alt=''></a>\n" +
                     "<a href=\"#\" class='particulars'><div class='r_chakan' ><i style='font-style:normal'>查看<img  class='r_chakan_img' src='/remote-admin/statics/image/youjiantou.svg' alt=''></div></a>\n" +
                     "</td>\n" +
                     "</tr>"
                }
                $("#div").append(html)

            //   删除
                var id
                $(".deleteq").click(function () {
                    $(".shade_delete,.shade_b_delete").css("display", "block");
                    id=$(this).parent().attr('id');
                })
                $(".sha_que_delete").click(function(){
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
                //删除彈窗/////////////////////
                $(".shade_a_delete,.sha_cancel_delete,.guan_sha").click(function () {
                    $(".shade_delete,.shade_b_delete").css("display", "none")
                })
            // 地图定位
                $(".ma_p").click(function(){
                    var longitude=$(this).attr("id")
                    var name=$(this).parent().siblings(".Na_me").html()
                    var searchUrl=encodeURI('../equipment/equipment.html?longitude='+longitude+"&name="+name)
                    location.href =searchUrl;
                })
            // 编辑
                var proid
                $(".modifier").click(function(){
                    $(".shade_modifier,.shade_b_modifier").css("display","block");
                     proid=$(this).parent().attr('id');
                    var r_por_num = $(this).parent().siblings(".Na_me").html();
                    $(".pro_name_b").val(r_por_num);
                    var r_por_num = $(this).parent().siblings(".Na_me").html();
                    $(".pro_s_b").val(r_por_num);
                    var r_rrna = $(this).parent().siblings(".exclusiveUser").html();
                    $("#select1_b option:selected").text(r_rrna);
                })
                $("#confirm_x").click(function(){
                    var pro_name_b= $(".pro_name_b").val()
                    var pro_s_b= $(".pro_s_b").val()
                    var select_b=$("#select1_b option:selected").attr("id");

                     console.log(select_b)
                    //正则  丸子
                    if(pro_name_b ==""||select_b=="") {
                        $(".mistake").css("display","block")
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
       var pro_s_b= $(".pro_s").val()
       var select=$("#select1 option:selected").attr("id");
       console.log(select)
        //正则  丸子
        if(pro_name ==""||pro_s_b==""||select=="" ) {
            $(".mistake").css("display","block")
        }else{
            $.ajax({
                url:baseURL + 'fun/project/add',
                contentType: "application/json;charset=UTF-8",
                type:"POST",
                data:JSON.stringify({
                    "projectName":pro_name,
                    "projectDesc":pro_s_b,
                    "exclusiveUser":select
                }),
                success: function(res){
                    $(".pro_name").val("")
                    $("#select1").val("")
                    $(".pro_s_b").val("")
                    window.location.reload()
                }
            });

        }

    })
    //编辑去弹窗/////////////////////////////////
     $(".shade_modifier_project").click(function(){
         $(".shade_modifier,.shade_b_modifier").css("display","none")
     })
    $(".rqubtn,.shade_a_delete").click(function () {
        $(".shade_delete,.shade_b_delete").css("display", "none")
    })
})



