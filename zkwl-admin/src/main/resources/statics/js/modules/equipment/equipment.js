$(function () {
//    抽屉
    $("#drawer_img").click(function(){
        if($("#drawer").attr("class") == "drawer"){
            $("#drawer").addClass("drawer_hid")
        }else{
            $("#drawer").removeClass("drawer_hid")
        }
    })
});