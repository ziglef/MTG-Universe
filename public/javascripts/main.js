var t;
var table;

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

    if($("#myCollections")) {
        table = $('#collection').DataTable({
            responsive: true
        });
        fillCards();


        $("#myCollections").change(function () {
            //alert( $('option:selected', this).text() );
            table.clear().draw();
            fillCards();
        });
    }else if($("#articleSelect")) {

        fillArticle();


        $("#articleSelect").change(function () {
            fillArticle();
        });
    }


    /*$( "#blob" ).hover(
        function() {
            $("#blob img" ).css( "display", "block");
        }, function() {
            $( "#blob img" ).css( "display", "none");
        }
    );*/


    $('[rel="popover"]').popover();

});


$(document).on("submit", "#createArticle", function(){

    content = document.createTextNode($('.Editor-editor' ).html());
    $("#atc").append(content);
    console.log(content);
    alert(content);

    $.ajax ( {
        url : '/addArticleToDB',
        contentType : false,
        data : new FormData(this),
        type : 'POST',
        processData: false,
        success : function ( data, textStatus, jqXHR ) {
            window.location.replace("/articles");
        },
        error : function ( jqXHR, textStatus, errorThrown ) {
            alert ( textStatus + ": " + errorThrown ) ;
        }
    } ) ;

    return false;
});

$(document).on("click", "#leftbar-toggle", function(){
    if( $("#leftbar .sidebar-container").hasClass("hidebar") )
        $("#leftbar .sidebar-container").removeClass("hidebar");
    else
        $("#leftbar .sidebar-container").addClass("hidebar");
});

$(document).on("click", "#rightbar-toggle", function(){

    if( $("#rightbar .sidebar-container").hasClass("hidebar") )
        $("#rightbar .sidebar-container").removeClass("hidebar");
    else
        $("#rightbar .sidebar-container").addClass("hidebar");
});

function fillArticle() {

    var id = $("#myArticles option:selected").attr("class").replace("col", "");

    var str = '{"colID": "' + id + '"}';
    $.ajax({
        url: '/getArticle',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: str,
        type: 'POST',
        success: function (data, textStatus, jqXHR) {

        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(textStatus + ": " + errorThrown);
        }
    });

}

function fillCards() {

    //if($("#myCollections option").children().length != 0) {
        var id = $("#myCollections option:selected").attr("class").replace("col", "");

        var str = '{"colID": "' + id + '"}';
        $.ajax({
            url: '/getCollectionCards',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: str,
            type: 'POST',
            success: function (data, textStatus, jqXHR) {

                console.log(data);
                data.forEach(function (obj) {

                    table.row.add([
                        "<tr><td width=\"50%\"><a href=\"#\" data-trigger=\"click\"  rel=\"popover\" data-container=\"body\" data-html=\"true\" data-content='<img class=\"img-responsive\" src=\"http://mtgimage.com/multiverseid/"+obj.multiverseid+".jpg\"/>' >" + obj.name + "</a></td>",
                        "<td width=\"20%\">" + obj.type + "</td>",
                        "<td width=\"10%\">" + obj.releaseDate + "</td>",
                        "<td class=\"text-center\" width=\"5%\"><i class=\"fa fa-check fa-success\"></i></td>",
                        "<td class=\"text-center\" width=\"5%\"><i class=\"fa fa-check fa-success\"></i></td>",
                        "</tr>",
                    ]).draw();

                });

                $('[rel="popover"]').popover();

            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus + ": " + errorThrown);
            }
        });
    //}
}

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
       "<button class=\"btn btn-sm btn-danger btn-block\" onclick=\"removeFromCollection(this,"+id+")\"> X </button>",
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

    //alert(id);
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

function removeCollection(){

    var id = $("#myCollections option:selected").attr("class").replace("col","");

    var str = '{"colID": "'+id+'"}';
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

function editCollection(){
    var id = $("#myCollections option:selected").attr("class").replace("col","");
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

$(document).on("click", "#makeSearch", function(){

    window.location.replace("searchResult/"+ $("#text2Search").val());

});
