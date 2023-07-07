/**-----------------------------------------------------------------------------------------
 * Description : main.js
 **----------------------------------------------------------------------------------------*/
/********************************************************************************
 * Global Variable : 전역변수 정의
 ********************************************************************************/

 //새로고침할때마다 업데이트되는 유가 데이터
 var oilPriceData;
 var table;

 //카카오맵
 var map;
 //카카오맵 마커
 var markerArr=[];
 var markerImage = new kakao.maps.MarkerImage('/images/gas-station_red.png', new kakao.maps.Size(69, 69), {offset: new kakao.maps.Point(27, 69)});

 var content = '<div class ="wrap">'+
                        '<div class="title">'+
                            '<div class="title-name">'+
                            '0000주유소'+
                            '</div>'+
                            '<button class="close-button">X</button>'+
                        '</div>'+
                        '<div class="contents">'+
                            '<ul class="info-ul">'+
                                '<li>주소 : 경기도 성남시 분당구</li>' +
                                '<li>브랜드 : SK 주유소</li>' +
                                '<li>전화번호 : 031-123-4567 </li>' +
                                '<li>세차장 : 있음 </li>' +
                                '<li>휘발유 : 999원 </li>' +
                                '<li>경유 : 999원 </li>' +
                                '<li>고급휘발유 : 999원 </li>' +
                            '</ul>'+
                            '<div class="manage-buttons">'+
                                '<button class="manage-button">찜하기</button>'+
                                '<button class="manage-button">가격알림</button>'+
                            '</div>'+
                        '</div>'+
                '</div>';

/********************************************************************************
 * Document Ready
 ********************************************************************************/

document.addEventListener("DOMContentLoaded", async function(){
    main.initHeaderLoad();
    main.initMapLoad();
    common.loadLocationSelectBox();

    //화면 로드될때 유가 최초 로드 후 tabulator에 입히기
    oilPriceData = await common.getOilPriceFetch('전체','','B027');
    oilPriceData = oilPriceData.RESULT.OIL;
    main.initTabulatorLoad();
    main.initEvent();
});

var main={
    initHeaderLoad : function(){
        common.headerLoad();
        common.navLoad();
    },

    initEvent:function(){
        var filterElements = document.querySelectorAll('#searchGungu, #priceSortRadio');
        document.querySelector('#searchSido').addEventListener("change", function(event){
            common.loadLocationSelectBox(document.getElementById('searchSido').value);
        });
        filterElements.forEach(function(filterElements) {
            filterElements.addEventListener("change", async function(event) {
                var sido = document.querySelector('#searchSido').value;
                var gungu = document.querySelector('#searchGungu').value;
                var oil = document.querySelector('input[name="priceSort"]:checked').value;
                oilPriceData = await common.getOilPriceFetch(sido,gungu,oil);
                table.replaceData(oilPriceData.RESULT.OIL);
            });
        });
        table.on("rowClick", async function(e, row){
            var myAddress = row.getData().NEW_ADR;
            var geo;
            geo = await common.getLatLngToAdr(myAddress);
            main.setCenterAndMarker(geo.latitude,geo.longitude);
        });
    },

    initMapLoad:function(){
        let container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
        let options = { //지도를 생성할 때 필요한 기본 옵션
        	center: new kakao.maps.LatLng(37.413293, 127.143591), //지도의 중심좌표.
        	level: 3 //지도의 레벨(확대, 축소 정도)
        };

        map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
    },
    initTabulatorLoad:function(){
        table = new Tabulator("#listTabulator", {
         	data:oilPriceData,
         	layout:"fitColumns",
         	pagination:true,
            paginationSize:5,
         	columns:[
        	 	{title:"주유소ID", field:"UNI_ID",     visible:false},
        	 	{title:"주유소명",  field:"OS_NM",      tooltip:true, width: "30%"},
        	 	{title:"브랜드",   field:"POLL_DIV_CD",tooltip:true, width: "15%"},
        	 	{title:"가격(원)", field:"PRICE",      width: "15%"},
        	 	{title:"주소",    field:"NEW_ADR",    tooltip:true,width: "40%"},
                {title:"구주소",   field:"VAN_ADR",    visible:false},
                {title:"GIS X좌표", field:"GIS_X_COOR", visible:false},
                {title:"GIS Y좌표", field:"GIS_Y_COOR", visible:false},
         	],
        });
    },

    //중간 위치 지정 후 지도마커 표시
    setCenterAndMarker:function(lat,lng){
    var moveLatLon = new kakao.maps.LatLng(lat,lng);
    var overlayLatLon = new kakao.maps.LatLng(lat+0.0003,lng+0.002);
    map.setCenter(moveLatLon);
    main.createMarker(moveLatLon,overlayLatLon);
    },

    createMarker:function(location,overlayLocation){//중심좌표, 오버레이위치
        function addMarker(map){
            for (var i = 0; i < markerArr.length; i++) {
                markerArr[i].setMap(map);
            }
        };
        var marker = new kakao.maps.Marker({
            position: location,
            image:markerImage
        });
        var customOverlay = new kakao.maps.CustomOverlay({
            position: overlayLocation,
            content: content
        });
        customOverlay.setMap(map);
        addMarker(null);
        markerArr=[];
        markerArr.push(marker);
        addMarker(map);
    },


}

