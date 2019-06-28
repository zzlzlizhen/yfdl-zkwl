$(function() {
    //分页
    var pages
    var pageSize
    var pageNum

    //选项卡
    $(".tab li").click(function () {
        $(".tab li").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
        $(".option_a>.option").hide().eq($(this).index()).show();
        //另一种方法: $("div").eq($(".tab li").index(this)).addClass("on").siblings().removeClass('on');
    });

    //详情去弹窗
    $(".shade_add_project,.glyphicon,.r_yes").click(function(){
        $(".shade_project").css("display","none")
    })
    r_fenye(10,1)
//系统消息
function r_fenye(pageSizea, pagesa){
    $.ajax({
        url: baseURL + '/msg/message/list',
        type: "GET",
        data: {
            "limit": pageSizea,
            "page": pagesa
        },
        success: function (res) {
            console.log("数据")
            console.log(res)
            pages = res.page.currPage;  //第几页
            pageSize = res.page.pageSize;//每页条数
            pageNum = res.page.totalPage;  //总页数
            console.log("222222222222222222222")
            console.log(pages)
            console.log(pageSize)
            console.log(pageNum)
            var inhtml=""
            for (var i = 0; i < res.page.list.length; i++) {
                inhtml +=
                    "<li>"+ '<img  src=\'"+res.page.list[i].headUrl+"\'>'+
                    "<p>"+
                    "<span>系统消息</span>"+
                    "<span>"+res.page.list[i].content+"</span>"+
                    "</p>"+
                    "<span>" + res.page.list[i].createDate+"</span>"+
                    "</li>"
            }
            $(".on ul").append(inhtml);
            //    详情
            $(".r_details").click(function(){
                $(".shade_project,.shade_b_project_r").css("display","block");
            })
            //  分頁
            $("#pagination5").pagination({
                currentPage: pages,
                totalPage: pageNum,
                isShow: true,
                count: 7,
                homePageText: "首页",
                endPageText: "尾页",
                prevPageText: "上一页",
                nextPageText: "下一页",
                callback: function (current) {
                    //当前页数current
                    var pagesb = current
                    $(".on").html("")
                    r_fenye(pageSize, pagesb)
                }
            });
        }
    })
}
    r_ffye()
    //反馈详情列表
  function r_ffye(pageSizea, pagesa){
      $.ajax({
          url: baseURL + '/sys/feedback/list',
          type: "GET",
          data: {
              "limit": pageSizea,
              "page": pagesa
          },
          success: function (res) {
              console.log("数据11111")
              console.log(res)
              pages = res.page.currPage;  //第几页
              pageSize = res.page.pageSize;//每页条数
              pageNum = res.page.totalPage;  //总页数
              var html="";

              for (var i = 0; i < res.page.list.length; i++) {
                  html +=
                      "<li>"+ '<img  src=\'"+res.page.list[i].headUrl+"\'>'+
                      "<p>"+
                      "<span>"+res.page.list[i].username+"</span>"+
                      "<span>"+res.page.list[i].backContent+"</span>"+
                      "</p>"+
                      " <div>"+
                      "<span >"+ res.page.list[i].backCreateTime+"</span>"+
                      "<span class='r_details' id="+ res.page.list[i].backId+">详情</span>"+
                      "</div>"+
                      "</li>"
              }

              $(".r_llis ul").append(html);
              //    详情
              $(".r_details").click(function(){
                  $(".shade_project,.shade_b_project_r").css("display","block");
              })
             //分页
              $("#pagination4").pagination({
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
                      $(".r_llis").html("")
                      form(pageSize, pagesb)
                  }
              });
          }
      })
  }

    $(".r_details").click(function(){
        var r_tents=$("#r_tents").html()
        var r_const= $("#r_const").html()
        var r_imes=$("#r_imes").html()
        var r_emls=$("#r_emls").html()
        var r_moes=$("#r_moes").html()
        var backId=$(this).attr('id');
        var r_imgs=$("#r_imgs").attr("src");
        rdele(backId);
        console.log(backId)

    })
    function rdele(backId){
        $.ajax({
            url: baseURL + '/sys/feedback/info/'+backId,
            type: "GET",
            success: function (res) {
                console.log("99999999999999999999999999")
                console.log(res)
                var headUrl = res.feedback.headUrl
                $("#r_imgs").attr("src",headUrl)
                var username = res.feedback.username
                $("#r_tents").html(username)
                var backCreateTime=res.feedback.backCreateTime
                $("#r_imes").html(backCreateTime)
                var backContent=res.feedback.backContent
                $("#r_const").html(backContent)
                var email=res.feedback.email
                $("#r_emls").html(email)
                var mobile=res.feedback.mobile
                $("#r_moes").html(mobile)
                if(res.code == "200"){
                }else{
                    alert("失败")
                }

            }
        })
    }




})