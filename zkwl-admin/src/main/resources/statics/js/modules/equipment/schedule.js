
function  zhuang(pro_je,groupId,par_id) {
    console.log("-------Id----"+par_id)
    $.ajax({
        url: baseURL + 'fun/device/getDeviceById?deviceId='+par_id,
        contentType: "application/json;charset=UTF-8",
        type: "get",
        data: {},
        success: function (res) {
            console.log("单台设备")
            console.log(res)
            console.log(res.data.onOff)
            $(".battery_a").html(res.data.batteryState)
            $(".battery_b").html(res.data.batteryMargin)
            $(".battery_c").html(res.data.batteryVoltage)

            $(".battery_d").html(res.data.photocellState)
            $(".battery_e").html(res.data.photovoltaicCellVoltage)
            $(".battery_f").html(res.data.chargingCurrent)
            $(".battery_g").html(res.data.chargingPower)

            $(".battery_h").html()
            $(".battery_i").html(res.data.loadVoltage)
            $(".battery_l").html(res.data.loadPower)
            $(".battery_o").html(res.data.loadCurrent)

            $(".control-label").click(function(){
                var category = $('input:radio[name="category"]:checked').val();
                console.log(category)

                $.ajax({
                    url: baseURL + 'fun/device/updateDevice',
                    contentType: "application/json;charset=UTF-8",
                    type:"POST",
                    data:JSON.stringify({
                        "projectId":pro_je,//项目id
                        "groupId":groupId,//分组id
                        "deviceId":par_id,//设备id
                        "lightingDuration":12,//亮灯时长
                        "morningHours":12,//晨亮时长
                        "light":12,//亮度
                        "onOff": category,//开关
                    }),
                    success: function (res) {
                        console.log("单台设备上传数据")
                        console.log(res)

                    }

                })
            })
        }

    })

    var scroll = document.getElementById('scrolla');
    var bar = document.getElementById('bara');
    var mask = document.getElementById('maska');
    var ptxt = document.getElementById('pa');
    var barleft = 0;
    bar.onmousedown = function(event){
        var event = event || window.event;
        var leftVal = event.clientX - this.offsetLeft;
        var that = this;
        // 拖动一定写到 down 里面才可以
        document.onmousemove = function(event){
            var event = event || window.event;
            barleft = event.clientX - leftVal;
            if(barleft < 0)
                barleft = 0;
            else if(barleft > scroll.offsetWidth - bar.offsetWidth)
                barleft = scroll.offsetWidth - bar.offsetWidth;
            mask.style.width = barleft +'px' ;
            that.style.left = barleft + "px";
            ptxt.innerHTML = parseInt(barleft/(scroll.offsetWidth-bar.offsetWidth) * 100) + "%";
            //防止选择内容--当拖动鼠标过快时候，弹起鼠标，bar也会移动，修复bug
            window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
        }

    }
    var scrollc = document.getElementById('scrollb');
    var barc = document.getElementById('barb');
    var maskc = document.getElementById('maskb');
    var ptxtc = document.getElementById('pb');
    var barleftc = 0;
    barc.onmousedown = function(event){
        var event = event || window.event;
        var leftValb = event.clientX - this.offsetLeft;
        var that = this;
        // 拖动一定写到 down 里面才可以
        document.onmousemove = function(event){
            var event = event || window.event;
            barleftc = event.clientX - leftValb;
            if(barleftc < 0)
                barleftc = 0;
            else if(barleftc > scrollc.offsetWidth - barc.offsetWidth)
                barleftc = scrollc.offsetWidth - barc.offsetWidth;
            maskc.style.width = barleftc +'px' ;
            that.style.left = barleftc + "px";
            ptxtc.innerHTML = parseInt(barleftc/(scrollc.offsetWidth-barc.offsetWidth) * 100) + "%";
            //防止选择内容--当拖动鼠标过快时候，弹起鼠标，bar也会移动，修复bug
            window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
        }

    }
    var scrollb = document.getElementById('scrollc');
    var barb = document.getElementById('barc');
    var maskb = document.getElementById('maskc');
    var ptxtb = document.getElementById('pc');
    var barleftb = 0;
    barb.onmousedown = function(event){
        var event = event || window.event;
        var leftVal = event.clientX - this.offsetLeft;
        var that = this;
        // 拖动一定写到 down 里面才可以
        document.onmousemove = function(event){
            var event = event || window.event;
            barleftb = event.clientX - leftVal;
            if(barleftb < 0)
                barleftb = 0;
            else if(barleftb > scrollb.offsetWidth - barb.offsetWidth)
                barleftb = scrollb.offsetWidth - barb.offsetWidth;
            maskb.style.width = barleftb +'px' ;
            that.style.left = barleftb + "px";
            ptxtb.innerHTML = parseInt(barleftb/(scrollb.offsetWidth-barb.offsetWidth) * 100) + "%";
            //防止选择内容--当拖动鼠标过快时候，弹起鼠标，bar也会移动，修复bug
            window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
        }

    }
    document.onmouseup = function(){
        document.onmousemove = null; //弹起鼠标不做任何操作
    }
}
