function activateMenu() {
    var path = location.pathname;
    /* Highlight current link */    
    $('.navbar li').removeClass('active');
    $('.navbar li:has(a[href="' + path + '"])').addClass('active')
}

$(document).ready(activateMenu)
