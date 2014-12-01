var t;

$(document).ready(function () {
    $('[data-toggle=offcanvas]').click(function () {
        $('.row-offcanvas').toggleClass('active');
    });


    $.expr[":"].contains = $.expr.createPseudo(function(arg) {
    return function( elem ) {
        return $(elem).text().toUpperCase().indexOf(arg.toUpperCase()) >= 0;
        };
    });

    $('select').selectpicker({
        size: 10
    });

    t = $('#example1').DataTable();

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

function addToCollection(name, id) {


    t.row.add( [
       "<a href=\"#\" name=\""+name+"\" onclick=\"changeImg ( this.name ) ;\">"+name+"</a>" ,
       "<button class=\"btn btn-sm btn-danger btn-block\" name=\""+name+"\" onclick=\"removeFromCollection(this,id)\"> X </button>",
    ] ).draw();

    var str = '{"colID": "'+$("h3.collection").attr("collection")+'",' +
              ' "cardID": "'+id+'"}';
    $.ajax ( {
        url : '/addCardToCollection',
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

function removeFromCollection(elem, id){
    $(elem).closest("tr").addClass("select");
    t.row('.select').remove().draw(false);

    alert(id);
    var str = '{"colID": "'+$("h3.collection").attr("collection")+'",' +
              ' "cardID": "'+id+'"}';
    $.ajax ( {
        url : '/deleteCardFromCollection',
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

function removeCollection(id){

    var str = '{"colID": "'+$("h3.collection").attr("collection")+'",' +
              ' "cardID": "'+id+'"}';
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

function editCollection(id){
    window.location.replace("/editCollection/"+id);
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
            window.location.replace(data["url"]);

        },
        error : function ( jqXHR, textStatus, errorThrown ) {
            alert ( textStatus + ": " + errorThrown ) ;
        }
    } ) ;
}


// jQuerys for page scrolling feature - requires jQuery Easing plugin
$(function() {
    $('a.page-scroll').on('click', function(event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: $($anchor.attr('href')).offset().top
        }, 1500, 'easeInOutExpo');
        event.preventDefault();
    });
});

