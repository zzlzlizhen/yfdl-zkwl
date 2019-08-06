$(function() {
    //分页
    var pages
    var pageSize
    var pageNum
    var pagesb
    var pageSizeb
    var pageNumb
    //选项卡
    $(".tab li").click(function () {
        $(".tab li").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
        $(".option_a>.option").hide().eq($(this).index()).show();
        //另一种方法: $("div").eq($(".tab li").index(this)).addClass("on").siblings().removeClass('on');
    });

    r_fenye(10,1)
    //系统消息
    function r_fenye(pageSizea, pagesa){
        $.ajax({
            url: baseURL + 'msg/message/list',
            type: "GET",
            data: {
                "limit": pageSizea,
                "page": pagesa
            },
            success: function (res) {
                pagesb = res.page.currPage;  //第几页
                pageSizeb = res.page.pageSize;//每页条数
                pageNumb = res.page.totalPage;  //总页数
                var inhtml=""
                for (var i = 0; i < res.page.list.length; i++) {
                    inhtml +=
                        "<li>"+
                        "<div class='D_img'>"+
                        "<img  src='/statics/image/lingDa.svg'>"+
                        "</div>"+
                        "<p>"+
                        "<span>系统消息</span>"+
                        "<span>"+res.page.list[i].content+"</span>"+
                        "</p>"+
                        "<span>" + res.page.list[i].createDate+"</span>"+
                        "</li>"
                }
                $(".r_op>ul").append(inhtml);

                //  分頁
                $("#pagination5").pagination({
                    currentPage: pagesb,
                    totalPage:pageNumb ,
                    isShow: true,
                    count: 7,
                    homePageText: "首页",
                    endPageText: "尾页",
                    prevPageText: "上一页",
                    nextPageText: "下一页",
                    callback: function (current) {
                        //当前页数current
                        var pagesba = current
                        $(".r_op>ul").html("")
                        r_fenye(pageSizeb, pagesba)
                    }
                });
            }
        })
    }
    r_ffye(10,1)
    //反馈详情列表
  function r_ffye(pageSizea, pagesa){
      $.ajax({
          url: baseURL + 'sys/feedback/list',
          type: "GET",
          data: {
              "limit": pageSizea,
              "page": pagesa
          },
          success: function (res) {
              pages = res.page.currPage;  //第几页
              pageSize = res.page.pageSize;//每页条数
              pageNum = res.page.totalPage;  //总页数
              var html="";
              for (var i = 0; i < res.page.list.length; i++) {
                  var head="https://guangxun-wulian.com"+res.page.list[i].headUrl
                  html +=
                      "<li id="+ res.page.list[i].uid+">"+
                      "<div class='D_img'>"+
                      "<img  src="+head+">"+
                      "</div>"+
                      "<p>"+
                      "<span>"+res.page.list[i].username+"</span>"+
                      "<span>"+res.page.list[i].backContent+"</span>"+
                      "</p>"+
                      " <div>"+
                      "<span >"+ res.page.list[i].backCreateTime+"</span>"+
                      "<span class='r_details' id="+ res.page.list[i].backId+"><img src='/statics/image/rxiangqing.png' alt=''> 详情</span>"+
                      "</div>"+
                      "</li>"
              }

              $(".r_llis>ul").append(html);
              //未读条数
              $.ajax({
                  url: baseURL + 'sys/feedback/queryCount ',
                  type: "GET",
                  success: function (res) {
                      var r_num_in = res.queryCount;
                      $("#r_num_in").html(r_num_in)
                      if(res.code == "200"){
                          console.log(res)
                      }else{
                          alert("失败")
                      }
                  }
              })
              // 详情
              var backId
              var uid
              $(".r_details").click(function(){
                  $(".shade_project,.shade_b_project_r").css("display","block");
                  backId=$(this).attr("id")
                  uid=$(this).parent().parent().attr("id")
                  rdele(backId);
              })

              $("#huifue").click(function(){
                  $(".roi").css("display","none")
                  $(".ryu").css("display","block")
              })
              $('#project_confirme').click(function(){
                  var rcon = $(".r_tex_bo").val()
                  $.ajax({
                      url: baseURL + 'sys/feedback/update',
                      type: "POST",
                      data:
                          "&backId=" + backId +
                          "&uid=" + uid +
                          "&answerContent=" + rcon ,
                      success: function (res) {
                          console.log(res)
                          if(res.code == "200"){
                              $(".shade_project").css("display","none")
                              $(".roi").css("display","none")
                              $(".ryu").css("display","block")
                          }else{
                              //alert("失败")
                              layer.open({
                                  title: '信息',
                                  content: res.msg,
                                  skin: 'demo-class'
                              });
                          }
                      }
                  })
              })


              function rdele(backId){
                  console.log(backId)
                  $.ajax({
                      url: baseURL + 'sys/feedback/info/'+backId,
                      type: "GET",
                      success: function (res) {
                          var headUrl = "https://guangxun-wulian.com"+res.feedback.headUrl;
                          $("#r_imgs").attr("src",headUrl)
                          var username = res.feedback.username;
                          $("#r_tents").html(username)
                          var backCreateTime=res.feedback.backCreateTime;
                          $("#r_imes").html(backCreateTime)
                          var backContent=res.feedback.backContent;
                          $("#r_const").html(backContent)
                          var email=res.feedback.email;
                          $("#r_emls").html(email)
                          var mobile=res.feedback.mobile;
                          $("#r_moes").html(mobile)
                          if(res.code == "200"){
                          }else{
                              //alert("失败")
                              layer.open({
                                  title: '信息',
                                  content: '失败',
                                  skin: 'demo-class'
                              });
                          }

                      }
                  })
              }

              //分页
              $("#pagination4").pagination({
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
                      $(".r_llis>ul").html("")
                      r_ffye(pageSize, pagesb)
                  }
              });
          }
      })
  }

    //详情去弹窗
    $(".shade_add_project,.glyphicon,.r_yes").click(function(){
        $(".shade_project").css("display","none")
        $(".roi").css("display","none")
        $(".ryu").css("display","block")
    })

    //回复
    $('.sha_buttom').find('div').click(function(){
        var index = $(this).index();
        // $(this).addClass('active').siblings().removeClass('active');
        $('.r_xc').find('.rionbo').eq(index).show().siblings().hide();
    })


})