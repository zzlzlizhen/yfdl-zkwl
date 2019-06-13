$(function () {
    var pages
    var pageSize
    var pageNum
    //总览
    $("#pandect").click(function () {
        window.open("../pandect/pandect.html")
    })

    //渲染
    form(10,1)

    function form(pageSizea,pagesa){
        $.ajax({
            url:baseURL + 'fun/project/queryProject',
            type:"POST",
            data:{"pageSize":pageSizea,"pageNum":pagesa},
            success: function(res){
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
                     "<td>"+res.data.list[i].sumCount+"</td>\n" +
                     "<td>"+res.data.list[i].gatewayCount+"</td>\n" +
                     "<td>"+res.data.list[i].faultCount+"</td>\n" +
                     "<td>"+res.data.list[i].callPoliceCount+"</td>\n" +
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
                    console.log(id)
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
                    console.log("编辑")
                    $(".shade_modifier,.shade_b_modifier").css("display","block");
                     proid=$(this).parent().attr('id');
                })
                $("#confirm_x").click(function(){
                    var pro_name_b= $(".pro_name_b").val()
                    var pro_s_b= $(".pro_s_b").val()
                    var select_b=$("#select1_b option:selected").text()
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
                            console.log(res)
                            window.location.reload()
                        }
                    });
                })
            // 分組
                $(".particulars").click(function(){
                    proid=$(this).parent().attr('id');
                    var Na_me=$(this).parent().siblings(".Na_me").html();
                    var exclusiveUser=$(this).parent().siblings(".exclusiveUser").html();
                    var searchUrl=encodeURI('../grouping/grouping.html?projectId='+proid+"&Na_me="+Na_me+"&exclusiveUser="+exclusiveUser)
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
                        var pagesb = current
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
                console.log(res)
                window.location.reload()
            }
        });
    })
    //编辑去弹窗/////////////////////////////////
     $(".shade_modifier_project").click(function(){
         $(".shade_modifier,.shade_b_modifier").css("display","none")
     })


})


