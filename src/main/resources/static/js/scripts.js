window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});
/********************************************************************************
 * common js 설정
 ********************************************************************************/
const common = {

    //headerLoad : 세션의 유무에 따라 다른 header 를 html load 해온다.
    headerLoad: function(){
        let info = document.querySelector('#info').value;
        let header = document.querySelector('#headerMenu');
        header.innerHTML = "";
        if(info == 'none'){
            header.innerHTML =
                 "<a class='navbar-brand ps-3 logo' href='/main'>만땅!</a>"
                +"<button class='btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0' id='sidebarToggle' href='#!'><i class='fas fa-bars'></i></button>"
                +"<form class='d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0'>"
                +       "<div class='input-group'>"
                +          "<input class='form-control' type='text' placeholder='주유소명 검색' aria-label='Search for...' aria-describedby='btnNavbarSearch' />"
                +           "<button class='btn btn-primary' id='btnNavbarSearch' type='button'><i class='fas fa-search'></i></button>"
                +       "</div>"
                +"</form>"
                +"<ul class='navbar-nav ms-auto ms-md-0 me-3 me-lg-4'>"
                +   "<li class='nav-item dropdown'>"
                +       "<a class='header-message' id='login' href='/login'>로그인  </a>"
                +       "<a class='header-message' id='join' href='/join'> 회원가입</a>"
                +   "</li>"
                +"</ul>"
        }else{
            header.innerHTML =
                 "<a class='navbar-brand ps-3 logo' href='/main/user'>만땅!</a>"
                +"<button class='btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0' id='sidebarToggle' href='#!'><i class='fas fa-bars'></i></button>"
                +"<form class='d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0'>"
                +       "<div class='input-group'>"
                +          "<input class='form-control' type='text' placeholder='주유소명 검색' aria-label='Search for...' aria-describedby='btnNavbarSearch' />"
                +           "<button class='btn btn-primary' id='btnNavbarSearch' type='button'><i class='fas fa-search'></i></button>"
                +       "</div>"
                +"</form>"
                +"<ul class='navbar-nav ms-auto ms-md-0 me-3 me-lg-4'>"
                +   "<li class='nav-item dropdown'>"
                +       "<a class='nav-link dropdown-toggle' id='navbarDropdown' href='#' role='button' data-bs-toggle='dropdown' aria-expanded='false'><i class='fas fa-user fa-fw'></i></a>"
                +       "<ul class='dropdown-menu dropdown-menu-end' aria-labelledby='navbarDropdown'>"
                +       "<li><a class='dropdown-item dropdown-name'>"+info+"님</a></li>"
                +       "<li><a class='dropdown-item' href='#!'>계정정보변경</a></li>"
                +       "<li><a class='dropdown-item' href='#!'>가격알림보기</a></li>"
                +       "<li><hr class='dropdown-divider' /></li>"
                +       "<li>"
                +       "<form method='post' action='/logout'>"
                +           "<button type='submit' class='dropdown-item dropdown-warning'>로그아웃</button>"
                +       "</form>"
                +   "</li>"
                +"</ul>"
        }

    },

    //navLoad : 세션의 유무에 따라 다른 nav 를 html load 해온다.
    navLoad: function(){
        let info = document.querySelector('#info').value;
        let nav = document.querySelector('#sidenavAccordion');
        nav.innerHTML = "";
        if(info == 'none'){
            nav.innerHTML =
                "<div class='sb-sidenav-menu'>"
                +   "<div class='nav'>"
                +       "<div class='sb-sidenav-menu-heading'>유가 정보</div>"
                +       "<a class='nav-link' href='/main'>"
                +           "<div class='sb-nav-link-icon'><i class='fas fa-tachometer-alt'></i></div>유가확인"
                +       "</a>"
                +       "<a class='nav-link' href='index.html'>"
                +           "<div class='sb-nav-link-icon'><i class='fas fa-tachometer-alt'></i></div>유가통계"
                +       "</a>"
                +   "</div>"
                +"</div>"
                +"<div class='sb-sidenav-footer'>"
                +   "<div class='small'>Logged in as:</div>Start Bootstrap"
                +"</div>"
        }else{
            nav.innerHTML =
                "<div class='sb-sidenav-menu'>"
                +   "<div class='nav'>"
                +       "<div class='sb-sidenav-menu-heading'>유가 정보</div>"
                +       "<a class='nav-link' href='/main/user'>"
                +           "<div class='sb-nav-link-icon'><i class='fas fa-tachometer-alt'></i></div>유가확인"
                +       "</a>"
                +       "<a class='nav-link' href='index.html'>"
                +           "<div class='sb-nav-link-icon'><i class='fas fa-tachometer-alt'></i></div>유가통계"
                +       "</a>"
                +       "<a class='nav-link' href='index.html'>"
                +           "<div class='sb-nav-link-icon'><i class='fas fa-tachometer-alt'></i></div>찜목록"
                +       "</a>"
                +   "</div>"
                +"</div>"
                +"<div class='sb-sidenav-footer'>"
                +   "<div class='small'>Logged in as:</div>Start Bootstrap"
                +"</div>"
        }

    },
    loadLocationSelectBox:function(sido){
        if(sido =='' ||sido == null ||sido == undefined){
            //초기 박스 로드시 sido 채우지 않은채 전달
            //sido 없을 시 big location에는 전체, small location에는 "==시도를 먼저 선택해 주세요=="
            var searchSido = document.querySelector("#searchSido");
            var searchGungu = document.querySelector("#searchGungu");

            var sidoOption = document.createElement("option");
            var gunguOption = document.createElement("option");

            for(var i=0;i<LOCATION_DATA.length;i++){
                sidoOption = document.createElement("option");
                sidoOption.text = LOCATION_DATA[i].sido;
                sidoOption.value = LOCATION_DATA[i].sido;
                searchSido.options.add(sidoOption);
            };

            searchSido[0].selected = true;
            gunguOption.text = "==시도를 먼저 선택해 주세요==";
            gunguOption.value = "";
            searchGungu.options.add(gunguOption);
            searchGungu[0].selected = true;
        }else{
            //console.log(sido);
            var searchGungu = document.querySelector("#searchGungu");
            var gunguOption = document.createElement("option");
            searchGungu.options.length=0;
            for(var i=0;i<LOCATION_DATA.length;i++){
                if(LOCATION_DATA[i].sido == sido){
                    for(var j=0;j<LOCATION_DATA[i].gungu.length;j++){
                        gunguOption = document.createElement("option");
                        gunguOption.text = LOCATION_DATA[i].gungu[j];
                        gunguOption.value = LOCATION_DATA[i].gungu[j];
                        searchGungu.options.add(gunguOption);
                    };
                }
            }
        }
    },

    //비동기 Area 데이터 호출
    //사용방법 : await common.getAreaFetch();
    //사용하지 않고 예시로만 남겨놓을 것
    getAreaFetch:function(){
        try{
            const response = fetch("/manage/getArea");
            return response.then(res=>res.json());
        }catch(error){
            console.log("지역정보 수신중 에러 발생",error);
        }
    },

    //비동기 유가정보 데이터 호출
    getOilPriceFetch:function(sido,gungu,oil){
        let filter = {
            sido_nm:sido,
            area_nm:gungu,
            prod_cd:oil
            }
        try{
            const response = fetch("/search/getOilPrice",{
                method:"POST",
                body:JSON.stringify(filter),
                headers:{'Content-Type': 'application/json'},
                });
            return response.then(res=>res.json());
        }catch(error){
            console.log("유가정보 수신중 에러 발생",error);
        }
    },

    //google geocoding 을 이용한 비동기식 주소-> 위경도 변환기
    getLatLngToAdr: function(adr){
        let filter = {
            address:adr
        }
        try{
            const response = fetch("/getLatLng",{
                method:"POST",
                body:JSON.stringify(filter),
                headers:{'Content-Type':'application/json'},
            });
            return response.then(res=>res.json());
        }catch(error){
            console.log("위경도 수신중 에러 발생",error);
        }
    },

    getStationDetailInfo : function(stationId){
        let filter = {
            id:stationId
        }
        try{
            const response = fetch("/getStationDetailInfo",{
                method:"POST",
                body:JSON.stringify(filter),
                headers:{'Content-Type':'application/json'},
            });
            return response.then(res=>res.json());
        }catch(error){
            console.log("주유소 상세정보 수신중 에러 발생",error);
        }
    },

}