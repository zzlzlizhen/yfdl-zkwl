
function  zhuang(pro_je,groupId,par_id) {
    $.ajax({
        url: baseURL + 'fun/device/getDeviceById?deviceId='+par_id,
        contentType: "application/json;charset=UTF-8",
        type: "get",
        data: {},
        success: function (res) {
            console.log("单台设备")
            console.log(res)
            $(".battery_a").html(res.data.batteryState);
            $(".battery_b").html(res.data.batteryMargin);
            $(".battery_c").html(res.data.batteryVoltage);

            $(".battery_d").html(res.data.photocellState);
            $(".battery_e").html(res.data.photovoltaicCellVoltage);
            $(".battery_f").html(res.data.chargingCurrent);
            $(".battery_g").html(res.data.chargingPower);

            $(".battery_h").html();
            $(".battery_i").html(res.data.loadVoltage);
            $(".battery_l").html(res.data.loadPower);
            $(".battery_o").html(res.data.loadCurrent);
            var luminance=res.data.light;
            var lian_g=res.data.lightingDuration;
            var chen_g=res.data.morningHours;
            $("#slideTest1").children().children(".layui-slider-bar").width((luminance/$(".progres").width()*$(".progres").width())+"%");
            $("#slideTest1").children().children(".layui-slider-tips").html(luminance)
            $("#slideTest1 .layui-slider-wrap").css('left', luminance/$(".progres").width()*$(".progres").width() + '%');

            $("#slideTest2").children().children(".layui-slider-bar").width((lian_g/$(".progres").width()*$(".progres").width())+"%");
            $("#slideTest2").children().children(".layui-slider-tips").html(lian_g)
            $("#slideTest2 .layui-slider-wrap").css('left', lian_g/$(".progres").width()*$(".progres").width() + '%');

            $("#slideTest3").children().children(".layui-slider-bar").width((chen_g/$(".progres").width()*$(".progres").width())+"%");
            $("#slideTest3").children().children(".layui-slider-tips").html(chen_g)
            $("#slideTest3 .layui-slider-wrap").css('left', chen_g/$(".progres").width()*$(".progres").width() + '%');

            var youVal = res.data.onOff; // 1,2 把拿到的开关灯值赋给单选按钮
            $("input[name='category']").each(function(index) {
                if ($("input[name='category']").get(index).value == youVal) {
                    $("input[name='category']").get(index).checked = true;
                }
            });



            //点击开关
            $(".control-label").click(function(){
                var category = $('input:radio[name="category"]:checked').val();
                var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
                var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
                var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

                var r_wena =  r_wen1/$(".progres").width()*100
                var r_wen_a = Math.ceil(r_wena);
                var r_wenb =  r_wen2/$(".progres").width()*100
                var r_wen_b = Math.ceil(r_wenb);
                var r_wenc =  r_wen3/$(".progres").width()*100
                var r_wen_c = Math.ceil(r_wenc);
                equipment_a(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c)

            })
        //    滑动单台亮度
            $('#slideTest1').blur(function(){
                var category = $('input:radio[name="category"]:checked').val();
                var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
                var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
                var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

                var r_wena =  r_wen1/$(".progres").width()*100
                var r_wen_a = Math.ceil(r_wena);
                var r_wenb =  r_wen2/$(".progres").width()*100
                var r_wen_b = Math.ceil(r_wenb);
                var r_wenc =  r_wen3/$(".progres").width()*100
                var r_wen_c = Math.ceil(r_wenc);
                equipment_a(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c)
            })
            //    滑动亮灯时长
            $('#slideTest2').blur(function(){
                var category = $('input:radio[name="category"]:checked').val();
                var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
                var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
                var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

                var r_wena =  r_wen1/$(".progres").width()*100
                var r_wen_a = Math.ceil(r_wena);
                var r_wenb =  r_wen2/$(".progres").width()*100
                var r_wen_b = Math.ceil(r_wenb);
                var r_wenc =  r_wen3/$(".progres").width()*100
                var r_wen_c = Math.ceil(r_wenc);
                equipment_a(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c)
            })
            //    滑动晨亮时长
            $('#slideTest3').blur(function(){
                var category = $('input:radio[name="category"]:checked').val();
                var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
                var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
                var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

                var r_wena =  r_wen1/$(".progres").width()*100
                var r_wen_a = Math.ceil(r_wena);
                var r_wenb =  r_wen2/$(".progres").width()*100
                var r_wen_b = Math.ceil(r_wenb);
                var r_wenc =  r_wen3/$(".progres").width()*100
                var r_wen_c = Math.ceil(r_wenc);
                equipment_a(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c)
            })


        }

    })
//    单台设备结束
}

//控制分组
function  f_en(pro_je,groupId) {
//点击开关
    $(".control-label").click(function(){
        var category = $('input:radio[name="category"]:checked').val();
        var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
        var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
        var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

        var r_wena =  r_wen1/$(".progres").width()*100
        var r_wen_a = Math.ceil(r_wena);
        var r_wenb =  r_wen2/$(".progres").width()*100
        var r_wen_b = Math.ceil(r_wenb);
        var r_wenc =  r_wen3/$(".progres").width()*100
        var r_wen_c = Math.ceil(r_wenc);
        equipment_b(pro_je,groupId,category,r_wen_a,r_wen_b,r_wen_c)

    })
    //    滑动单台亮度
    $('#slideTest1').blur(function(){
        var category = $('input:radio[name="category"]:checked').val();
        var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
        var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
        var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

        var r_wena =  r_wen1/$(".progres").width()*100
        var r_wen_a = Math.ceil(r_wena);
        var r_wenb =  r_wen2/$(".progres").width()*100
        var r_wen_b = Math.ceil(r_wenb);
        var r_wenc =  r_wen3/$(".progres").width()*100
        var r_wen_c = Math.ceil(r_wenc);
        equipment_b(pro_je,groupId,category,r_wen_a,r_wen_b,r_wen_c)
    })
    //    滑动亮灯时长
    $('#slideTest2').blur(function(){
        var category = $('input:radio[name="category"]:checked').val();
        var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
        var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
        var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

        var r_wena =  r_wen1/$(".progres").width()*100
        var r_wen_a = Math.ceil(r_wena);
        var r_wenb =  r_wen2/$(".progres").width()*100
        var r_wen_b = Math.ceil(r_wenb);
        var r_wenc =  r_wen3/$(".progres").width()*100
        var r_wen_c = Math.ceil(r_wenc);
        equipment_b(pro_je,groupId,category,r_wen_a,r_wen_b,r_wen_c)
    })
    //    滑动晨亮时长
    $('#slideTest3').blur(function(){
        var category = $('input:radio[name="category"]:checked').val();
        var r_wen1 = $("#slideTest1").children().children(".layui-slider-bar").width();
        var r_wen2 = $("#slideTest2").children().children(".layui-slider-bar").width();
        var r_wen3 = $("#slideTest3").children().children(".layui-slider-bar").width();

        var r_wena =  r_wen1/$(".progres").width()*100
        var r_wen_a = Math.ceil(r_wena);
        var r_wenb =  r_wen2/$(".progres").width()*100
        var r_wen_b = Math.ceil(r_wenb);
        var r_wenc =  r_wen3/$(".progres").width()*100
        var r_wen_c = Math.ceil(r_wenc);
        equipment_b(pro_je,groupId,category,r_wen_a,r_wen_b,r_wen_c)
    })
}
//控制设备
  function equipment_a(pro_je,groupId,par_id,category,r_wen_a,r_wen_b,r_wen_c) {
      $.ajax({
          url: baseURL + 'fun/device/updateDevice',
          contentType: "application/json;charset=UTF-8",
          type:"POST",
          data:JSON.stringify({
              "projectId":pro_je,//项目id
              "groupId":groupId,//分组id
              "deviceId":par_id,//设备id
              "lightingDuration":r_wen_b,//亮灯时长
              "morningHours":r_wen_c,//晨亮时长
              "light":r_wen_a,//亮度
              "onOff": category,//开关
          }),
          success: function (res) {
              console.log(res)
              if(res.code == "200"){

              }
          }

      })
  }
function equipment_b(pro_je,groupId,category,r_wen_a,r_wen_b,r_wen_c) {
    $.ajax({
        url: baseURL + 'fun/device/updateOnOffByIds',
        contentType: "application/json;charset=UTF-8",
        type:"POST",
        data:JSON.stringify({
            "projectId":pro_je,//项目id
            "groupId":groupId,//分组id
            "lightingDuration":r_wen_b,//亮灯时长
            "morningHours":r_wen_c,//晨亮时长
            "light":r_wen_a,//亮度
            "onOff": category,//开关
        }),
        success: function (res) {
            console.log(res)
            if(res.code == "200"){

            }
        }

    })
}
// var scroll = document.getElementById('scrolla');
// var bar = document.getElementById('bara');
// var mask = document.getElementById('maska');
// var ptxt = document.getElementById('pa');
// var barleft = 0;
// bar.onmousedown = function(event){
//     var event = event || window.event;
//     var leftVal = event.clientX - this.offsetLeft;
//     var that = this;
//     // 拖动一定写到 down 里面才可以
//     document.onmousemove = function(event){
//         var event = event || window.event;
//         barleft = event.clientX - leftVal;
//         if(barleft < 0)
//             barleft = 0;
//         else if(barleft > scroll.offsetWidth - bar.offsetWidth)
//             barleft = scroll.offsetWidth - bar.offsetWidth;
//         mask.style.width = barleft +'px' ;
//         that.style.left = barleft + "px";
//         ptxt.innerHTML = parseInt(barleft/(scroll.offsetWidth-bar.offsetWidth) * 100) + "%";
//         //防止选择内容--当拖动鼠标过快时候，弹起鼠标，bar也会移动，修复bug
//         window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
//     }
//
// }
//
// var scrollc = document.getElementById('scrollb');
// var barc = document.getElementById('barb');
// var maskc = document.getElementById('maskb');
// var ptxtc = document.getElementById('pb');
// var barleftc = 0;
// barc.onmousedown = function(event){
//     var event = event || window.event;
//     var leftValb = event.clientX - this.offsetLeft;
//     var that = this;
//     // 拖动一定写到 down 里面才可以
//     document.onmousemove = function(event){
//         var event = event || window.event;
//         barleftc = event.clientX - leftValb;
//         if(barleftc < 0)
//             barleftc = 0;
//         else if(barleftc > scrollc.offsetWidth - barc.offsetWidth)
//             barleftc = scrollc.offsetWidth - barc.offsetWidth;
//         maskc.style.width = barleftc +'px' ;
//         that.style.left = barleftc + "px";
//         ptxtc.innerHTML = parseInt(barleftc/(scrollc.offsetWidth-barc.offsetWidth) * 100) + "%";
//         //防止选择内容--当拖动鼠标过快时候，弹起鼠标，bar也会移动，修复bug
//         window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
//     }
//
// }
// var scrollb = document.getElementById('scrollc');
// var barb = document.getElementById('barc');
// var maskb = document.getElementById('maskc');
// var ptxtb = document.getElementById('pc');
// var barleftb = 0;
// barb.onmousedown = function(event){
//     var event = event || window.event;
//     var leftVal = event.clientX - this.offsetLeft;
//     var that = this;
//     // 拖动一定写到 down 里面才可以
//     document.onmousemove = function(event){
//         var event = event || window.event;
//         barleftb = event.clientX - leftVal;
//         if(barleftb < 0)
//             barleftb = 0;
//         else if(barleftb > scrollb.offsetWidth - barb.offsetWidth)
//             barleftb = scrollb.offsetWidth - barb.offsetWidth;
//         maskb.style.width = barleftb +'px' ;
//         that.style.left = barleftb + "px";
//         ptxtb.innerHTML = parseInt(barleftb/(scrollb.offsetWidth-barb.offsetWidth) * 100) + "%";
//         //防止选择内容--当拖动鼠标过快时候，弹起鼠标，bar也会移动，修复bug
//         window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
//     }
//
// }
// document.onmouseup = function(){
//     document.onmousemove = null; //弹起鼠标不做任何操作
// }
