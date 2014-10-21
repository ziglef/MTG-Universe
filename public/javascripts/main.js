/*
 $(function(){
 var textfield = $("input[name=user]");
 $('button[type="submit"]').click(function(e) {
 e.preventDefault();
 //little validation just to check username
 if (textfield.val() != "") {
 //$("body").scrollTo("#output");
 $("#output").addClass("alert alert-success animated fadeInUp").html("Welcome back " + "<span style='text-transform:uppercase'>" + textfield.val() + "</span>");
 $("#output").removeClass(' alert-danger');
 $("input").css({
 "height":"0",
 "padding":"0",
 "margin":"0",
 "opacity":"0"
 });
 //change button text
 $('button[type="submit"]').html("continue")
 .removeClass("btn-info")
 .addClass("btn-default").click(function(){
 $("input").css({
 "height":"auto",
 "padding":"10px",
 "opacity":"1"
 }).val("");
 });

 //show avatar
 $(".avatar").css({
 "background-image": "url('http://api.randomuser.me/0.3.2/portraits/women/35.jpg')"
 });
 } else {
 //remove success mesage replaced with error message
 $("#output").removeClass(' alert alert-success');
 $("#output").addClass("alert alert-danger animated fadeInUp").html("sorry enter a username ");
 }
 //console.log(textfield.val());

 });
 });

 */

var t;

$(document).ready(function () {
    $('[data-toggle=offcanvas]').click(function () {
        $('.row-offcanvas').toggleClass('active');
    });

    $('#example').DataTable({
        "pagingType": "simple"
    });

    t = $('#example1').DataTable({
        "pagingType": "simple"
    });


});

function changeImg(name) {
    $(".img-card").attr("src", "http://mtgimage.com/card/" + name + ".jpg");
}

function addToCollection(name) {
    //$("#example1 thead").append("<tr><td>" + name + "</td><td><button class=\"btn btn-sm btn-danger btn-block\"  name=\"" + name + "\"> X </button></i></td></tr>");

    var counter = 1;

    t.row.add( [
       "<a href=\"#\" name=\""+name+"\" onclick=\"changeImg ( this.name ) ;\">"+name+"</a>" ,
       "<button class=\"btn btn-sm btn-danger btn-block\" name=\""+name+"\" onclick=\"removeFromCollection(this)\"> X </button>",
    ] ).draw();

    counter++;
}

function removeFromCollection(elem){
    $(elem).closest("tr").addClass("select");
    t.row('.select').remove().draw(false);
}

