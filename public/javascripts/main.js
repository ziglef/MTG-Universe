var t;

// jQuery to collapse the navbar on scroll
$(window).scroll(function() {
    if ($(".navbar").offset().top > 50) {
        $(".navbar-fixed-top").addClass("top-nav-collapse");
    } else {
        $(".navbar-fixed-top").removeClass("top-nav-collapse");
    }
});

// jQuerys for page scrolling feature - requires jQuery Easing plugin
$(function() {
    $('a.page-scroll').bind('click', function(event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: $($anchor.attr('href')).offset().top
        }, 1500, 'easeInOutExpo');
        event.preventDefault();
    });
});


$(function() {
    $('.logo').bind('click', function(event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: $($anchor.attr('href')).offset().top
        }, 1500, 'easeInOutExpo');
        event.preventDefault();
    });
});

jQuery(document).ready(function($){
    // browser window scroll (in pixels) after which the "back to top" link is shown
    var offset = 300,
    //browser window scroll (in pixels) after which the "back to top" link opacity is reduced
        offset_opacity = 1200,
    //duration of the top scrolling animation (in ms)
        scroll_top_duration = 700,
    //grab the "back to top" link
        $back_to_top = $('.cd-top');

    //hide or show the "back to top" link
    $(window).scroll(function(){
        ( $(this).scrollTop() > offset ) ? $back_to_top.addClass('cd-is-visible') : $back_to_top.removeClass('cd-is-visible cd-fade-out');
        if( $(this).scrollTop() > offset_opacity ) {
            $back_to_top.addClass('cd-fade-out');
        }
    });

    //smooth scroll to top
    $back_to_top.on('click', function(event){
        event.preventDefault();
        $('body,html').animate({
                scrollTop: 0 ,
            }, scroll_top_duration
        );
    });

});

$(document).ready(function () {
    $('[data-toggle=offcanvas]').click(function () {
        $('.row-offcanvas').toggleClass('active');
    });

    t = $('#collection').DataTable({
        "pagingType": "simple",
    });

    // Add custom JS here
    t.$('a[rel=popover]').popover({
      html: true,
      trigger: 'hover',
      placement: 'right',
      content: function(){return '<img src="'+$(this).data('img') + '" />';}
    }); 

    $.expr[":"].contains = $.expr.createPseudo(function(arg) {
    return function( elem ) {
        return $(elem).text().toUpperCase().indexOf(arg.toUpperCase()) >= 0;
        };
    });

});

$(document).on("click", "#myCollections li .remove", function(){
    $(this).closest("li").remove();
    if ( $('#myCollections').children().length == 0 ) {
        $('#myCollections').append("You don't have collections");
    }
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

function removeCollection(id){

    var str = '{"id": "'+id+'"}';
    $.ajax ( {
        url : '/deleteCollection',
        dataType : 'json',
        contentType : 'application/json; charset=utf-8',
        data : str,
        type : 'POST',
        success : function ( data, textStatus, jqXHR ) {

        },
        error : function ( jqXHR, textStatus, errorThrown ) {
            alert ( textStatus + ": " + errorThrown ) ;
        }
    } ) ;
}

//function createNewCollection(){
function backup(){
        if ( $('#myCollections').children().length == 0 ) {
    $('#myCollections').empty();
    }
    if (!$('#myCollections li:contains('+$('#newCollection .collection-name').val()+')').length ) {
        $('#myCollections').prepend('<li><a href="#">'+$('#newCollection .collection-name').val()+'</a><a href="#" class="badge pull-right label-danger remove">X</a><a href="#" class="badge pull-right label-warning">Edit</a></li>');
        $('#newCollection .collection-name').val('');
        $('#newCollection').modal('hide');
    }else{
        alert('Essa já existe');
    }
}

function createNewCollection ( ) {

    var str = '{"name": "'+$('input[name=collection-name]' ).val()+'", ' +
              ' "visibility": "'+$('.collection-privacy input[type="radio"]:checked').attr('class')+'"}';

    $.ajax ( {
        url : '/createNewCollection',
        dataType : 'json',
        contentType : 'application/json; charset=utf-8',
        data : str,
        type : 'POST',
        success : function ( data, textStatus, jqXHR ) {
            alert(data["id"]);
        },
        error : function ( jqXHR, textStatus, errorThrown ) {
            alert ( textStatus + ": " + errorThrown ) ;
        }
    } ) ;
}

var left_side_width = 220; //Sidebar width in pixels

$(function() {
    "use strict";

    //Enable sidebar toggle
    $("[data-toggle='offcanvas']").click(function(e) {
        e.preventDefault();

        //If window is small enough, enable sidebar push menu
        if ($(window).width() <= 992) {
            $('.row-offcanvas').toggleClass('active');
            $('.left-side').removeClass("collapse-left");
            $(".right-side").removeClass("strech");
            $('.row-offcanvas').toggleClass("relative");
        } else {
            //Else, enable content streching
            $('.left-side').toggleClass("collapse-left");
            $(".right-side").toggleClass("strech");
        }
    });

    //Add hover support for touch devices
    $('.btn').bind('touchstart', function() {
        $(this).addClass('hover');
    }).bind('touchend', function() {
        $(this).removeClass('hover');
    });

    /*
     * INITIALIZE BUTTON TOGGLE
     */
    $('.btn-group[data-toggle="btn-toggle"]').each(function() {
        var group = $(this);
        $(this).find(".btn").click(function(e) {
            group.find(".btn.active").removeClass("active");
            $(this).addClass("active");
            e.preventDefault();
        });

    });

    /* Sidebar tree view */
    $(".sidebar .treeview").tree();

    /* 
     * Make sure that the sidebar is streched full height
     **/
    function _fix() {
        //Get window height and the wrapper height
        var height = $(window).height() - $("body > .header").height() - ($("body > .footer").outerHeight() || 0);
        $(".wrapper").css("min-height", height + "px");
        var content = $(".wrapper").height();
        //If the wrapper height is greater than the window
        if (content > height)
            //then set sidebar height to the wrapper
            $(".left-side, html, body").css("min-height", content + "px");
        else {
            //Otherwise, set the sidebar to the height of the window
            $(".left-side, html, body").css("min-height", height + "px");
        }
    }

    _fix();
    //Fire when wrapper is resized
    $(".wrapper").resize(function() {
        _fix();
    });

});

(function($) {
    "use strict";

    $.fn.tree = function() {

        return this.each(function() {
            var btn = $(this).children("a").first();
            var menu = $(this).children(".treeview-menu").first();
            var isActive = $(this).hasClass('active-tree');

            //initialize already active menus
            if (isActive) {
                menu.show();
                btn.children(".fa-angle-left").first().removeClass("fa-angle-left").addClass("fa-angle-down");
            }
            //Slide open or close the menu on link click
            btn.click(function(e) {
                e.preventDefault();
                if (isActive) {
                    //Slide up to close menu
                    menu.slideUp();
                    isActive = false;
                    btn.children(".fa-angle-down").first().removeClass("fa-angle-down").addClass("fa-angle-left");
                    btn.parent("li").removeClass("active-tree");
                } else {
                    //Slide down to open menu
                    menu.slideDown();
                    isActive = true;
                    btn.children(".fa-angle-left").first().removeClass("fa-angle-left").addClass("fa-angle-down");
                    btn.parent("li").addClass("active-tree");
                }
            });

        });

    };

}(jQuery));

