var t;
var tcomments;
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

    if(document.getElementById('myCollections')) {
        table = $('#collection').DataTable({
            responsive: true
        });

        fillCards();


        $("#myCollections").change(function () {
            //alert( $('option:selected', this).text() );
            table.clear().draw();
            fillCards();
        });
    }else if(document.getElementById('articleSelect')) {

        fillArticle();


        $("#articleSelect").change(function () {
            tcomments.clear().draw();
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

    $("#commentEditor").Editor();

    tcomments = $ ( '#article-com' ).DataTable ( {
            bFilter : false,
            bInfo : false,
            aaSorting :[[ 0, 'desc' ] ],
            bLengthChange: false,
            oLanguage: {
                sEmptyTable: "Be the first one to comment.."
            }
        }
    ) ;

    $ ( '#send-comment' ).on ( 'click', function ( ) {

        var d = new Date ( ) ;

        var month = d.getMonth ( ) + 1 ;
        var day = d.getDate ( ) ;
        var hour = d.getHours ( ) ;
        var minute = d.getMinutes ( ) ;
        var second = d.getSeconds ( ) ;

        var date = d.getFullYear ( ) + '-' +
            ( ( '' + month ).length < 2 ? '0' : '' ) + month + '-' +
            ( ( '' + day ).length < 2 ? '0' : '' ) + day + ' ' +
            ( ( '' + hour ).length < 2 ? '0' : '' ) + hour + ':' +
            ( ( '' + minute ).length < 2 ? '0' : '' ) + minute + ':' +
            ( ( '' + second ).length < 2 ? '0' : '' ) + second ;

        content = $(".commenting .Editor-editor").html();
        usersplit = this.value.split("|");
        username = usersplit[0];
        userid = usersplit[1];
        userimage = usersplit[2];
        articleid = getActualArticle();

        var str = '{ "artID": "' + articleid + '", '+
            '"writerID": "' + userid + '", '+
            '"date": "' + date + '", '+
            '"text": "' + text + '" }';

        $.ajax ( {
            url : '/addComment',
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


        tcomments.row.add ([
            addCommentLine(userimage, username, date, content)
        ] ).draw ( ) ;
        $(".commenting .Editor-editor").empty();
    } ) ;

});

function addCommentLine(userimage, username, date, content) {
    return '<div class="media"> <a class="pull-left" href="#"><img class="media-object" src="assets/images/avatar/'+userimage+'" alt=""> </a> <div class=" media - body "> <span class=" comment - username "><i class=" fa fa - user "></i><a href=" /profile/'+username+'"> '+username+'</a></span><span class=" comment - data "><i class=" fa fa - calendar "></i> '+date+'</span><br>'+content+'</div>';
}

function getActualArticle() {
    return $("#articleSelect option:selected").attr("class").replace("art", "");
}

function getActualCollection() {
    return $("#myCollections option:selected").attr("class").replace("col", "");
}

function removeArticle(){

    id = getActualArticle();

    var str = '{"artID": "'+id+'"}';
    $.ajax ( {
        url : '/deleteArticle',
        dataType : 'json',
        contentType : 'application/json; charset=utf-8',
        data : str,
        type : 'POST',
        success : function ( data, textStatus, jqXHR ) {
            window.location.replace("/articles");
        },
        error : function ( jqXHR, textStatus, errorThrown ) {
            alert ( textStatus + ": " + errorThrown ) ;
        }
    } ) ;
}

$(document).on("submit", "#editArticle", function(){
    content = document.createTextNode($('.Editor-editor' ).html());
    $("#atc").append(content);
    //console.log(content);

    $.ajax ( {
        url : '/addEditedArticleToDB',
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

$(document).on("submit", "#createArticle", function(){

    content = document.createTextNode($('.Editor-editor' ).html());
    $("#atc").append(content);
    console.log(content);

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
    $("#fillarticle").empty();

    var elem = document.getElementById('articleSelect');
    if(elem != null) {

        var id = getActualArticle();

        var str = '{"artID": "' + id + '"}';

        $.ajax({
            url: '/getArticle',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: str,
            type: 'POST',
            success: function (article, textStatus, jqXHR) {
                console.log(article);
                $('#fillarticle').append(
                '<div class="col-md-12" >'+
                '<h1 style="border-bottom: 1px solid #333;padding-bottom: 10px;">'+article.title+'</h1>'+
                '</div>'+
                '<div class="col-md-3">'+
                '<img src="assets/images/articles/'+article.imageUrl+'" class="profile-pic img-responsive" alt="avatar" />'+
                //'@if(resource("public/images/articles/" + userArticles.get(0).get("id") + ".png").isDefined) {'+
                //'<img src="@routes.Assets.at("images/articles/" + userArticles.get(0).get("id") + ".png")" class="profile-pic img-responsive" alt="avatar" />'+
                //'} else {'+
                //'<img src="@routes.Assets.at("images/avatar/default-avatar.png")" class="profile-pic img-responsive" alt="avatar" />'+
                //'}'+
                '<div class="col-md-6 text-center">'+
                '<i class="fa fa-user"></i> '+ article.writer.username+'<br>'+
                '</div>'+
                '<div class="col-md-6 text-center">'+
                '<i class="fa fa-calendar"></i> '+article.date+
                '</div>'+
                '</div>'+
                '<div class="col-md-9">'+
                '<p class="text-justify article-text">'+article.text+'</p>'+
                '</div>'
                );
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus + ": " + errorThrown);
            }
        });

        $.ajax({
            url: '/getArticleComments',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: str,
            type: 'POST',
            success: function (comments, textStatus, jqXHR) {
                console.log(comments);
                comments.forEach(function(comment) {

                    tcomments.row.add ([
                        addCommentLine(comment.commentWriter.imageurl, comment.commentWriter.username, comment.date, comment.text)
                    ] ).draw ( ) ;
                });

                $("#commentArea").css( "display", "");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus + ": " + errorThrown);
            }
        });
    }
}

function fillCards() {
    var elem = document.getElementById('myCollections');
    if(elem != null) {

        var id = getActualCollection();

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
                        "<tr><td width=\"35%\"><a href=\"card/" +obj.multiverseid+ "\" data-trigger=\"click\"  rel=\"popover\" data-container=\"body\" data-html=\"true\" data-content='<img class=\"img-responsive\" src=\"http://mtgimage.com/multiverseid/"+obj.multiverseid+".jpg\"/>' >" + obj.name + "</a></td>",
                        "<td width=\"20%\">" + obj.type + "</td>",
                        "<td width=\"25%\">" + obj.edition + "</td>",
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
    }
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

    var str = '{"colID": "'+$("h3.collection").attr("collection")+'",' +
              ' "cardID": "'+id+'"}';
    $.ajax ( {
        url : '/addCardToCollection',
        dataType : 'json',
        contentType : 'application/json; charset=utf-8',
        data : str,
        type : 'POST',
        success : function ( data, textStatus, jqXHR ) {
            if (data.toString() == "ok") {
                t.row.add([
                    "<a href=\"#\" name=\"" + name + "\" onclick=\"changeImg ( this.name ) ;\">" + name + "</a>",
                    "<button class=\"btn btn-sm btn-danger btn-block\" onclick=\"removeFromCollection(this," + id + ")\"> X </button>",
                ]).draw();
            }
        },
        error : function ( jqXHR, textStatus, errorThrown ) {
            alert ( textStatus + ": " + errorThrown ) ;
        }
    } ) ;

}

function removeFromCollection(elem, id){
    $(elem).closest("tr").addClass("select");
    t.row('.select').remove().draw(false);

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

    var id = getActualCollection();

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
    var id = getActualCollection();
    window.location.replace("/editCollection/"+id);
}

function editArticle(){
    var id = getActualArticle();
    window.location.replace("/editArticle/"+id);
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

