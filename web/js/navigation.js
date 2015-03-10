
navigateHome();


function navigateHome() {
    hideAll();
    jQuery.ajax({
        url: "/Developers.Astropay/html/frames/home.html", dataType: "html"
    }).done(function (responseHtml) {
        $("#main-content").html(responseHtml);
    });
}

function navigateAPD() {
    hideAll();
    $("#apd-menu").css("display", "block");

    jQuery.ajax({
        url: "/Developers.Astropay/html/frames/apd-frame.html", dataType: "html"
    }).done(function (responseHtml) {
        $("#main-content").html(responseHtml);

        jQuery.ajax({
            url: "/Developers.Astropay/html/frames/common/considerations.html", dataType: "html"
        }).done(function (responseHtml) {
            $("#considerations-container").html(responseHtml);
        });
    });

    $('.btn').each(function () {
        $(this).blur();
    });
}

function navigateFullAPI() {
    hideAll();
    $("#full-api-menu").css("display", "block");

    jQuery.ajax({
        url: "/Developers.Astropay/html/frames/full-api-frame.html", dataType: "html"
    }).done(function (responseHtml) {
        $("#main-content").html(responseHtml);

        jQuery.ajax({
            url: "/Developers.Astropay/html/frames/common/considerations.html", dataType: "html"
        }).done(function (responseHtml) {
            $("#considerations-container").html(responseHtml);
        });
    });

    $('.btn').each(function () {
        $(this).blur();
    });
}

function navigateRounders() {
    hideAll();
    $("#rounders-menu").css("display", "block");

    jQuery.ajax({
        url: "/Developers.Astropay/html/frames/rounders-frame.html", dataType: "html"
    }).done(function (responseHtml) {
        $("#main-content").html(responseHtml);
    });

    $('.btn').each(function () {
        $(this).blur();
    });
}

function navigateCashout() {
    hideAll();
    $("#cashout-menu").css("display", "block");

    jQuery.ajax({
        url: "/Developers.Astropay/html/frames/cashout-frame.html", dataType: "html"
    }).done(function (responseHtml) {
        $("#main-content").html(responseHtml);

        jQuery.ajax({
            url: "/Developers.Astropay/html/frames/common/considerations.html", dataType: "html"
        }).done(function (responseHtml) {
            $("#considerations-container").html(responseHtml);
        });
    });

    $('.btn').each(function () {
        $(this).blur();
    });
}

function hideAll() {
    $("#apd-menu").css("display", "none");
    $("#full-api-menu").css("display", "none");
    $("#rounders-menu").css("display", "none");
    $("#cashout-menu").css("display", "none");
}

