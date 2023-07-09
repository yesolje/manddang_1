/**-----------------------------------------------------------------------------------------
 * Description : main.js
 **----------------------------------------------------------------------------------------*/
/********************************************************************************
 * Global Variable : 전역변수 정의
 ********************************************************************************/

 //새로고침할때마다 업데이트되는 유가 데이터
 var oilPriceData;
 var table;

 //카카오맵,마커,오버레이 관련 객체
 var map;
 var marker;
 var markerArr=[];
 var markerImage = new kakao.maps.MarkerImage('/images/gas-station_green.png', new kakao.maps.Size(69, 69), {offset: new kakao.maps.Point(27, 69)});
 var mapOverlay;

 //객체_주유소 상세정보
 var stationDetailInfo;



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
            var clickedAddress = row.getData().NEW_ADR;
            var clickedStationId = row.getData().UNI_ID;
            var geo;
            geo = await common.getLatLngToAdr(clickedAddress);//주유소 위경도좌표
            stationDetailInfo = await common.getStationDetailInfo(clickedStationId);//주유소 상세정보
            stationDetailInfo = stationDetailInfo.RESULT.OIL[0];
            stationDetailInfo = main.addOilPriceInStationDetailInfo(stationDetailInfo);
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
        if(typeof mapOverlay !== 'undefined'){
            main.closeOverlay();
        }
        function addMarker(map){
            for (var i = 0; i < markerArr.length; i++) {
                markerArr[i].setMap(map);
            }
        };
        marker = new kakao.maps.Marker({
            position: location,
            image:markerImage
        });

        var overlayContent = `
         <div class ="wrap">
             <div class="title">
                 <div class="title-name"> ${stationDetailInfo.OS_NM}</div>
                 <button class="close-button" onclick="main.closeOverlay()">X</button>
             </div>
             <div class="contents">
                 <ul class="info-ul">
                     <li class="info-li">주소 : ${stationDetailInfo.NEW_ADR}</li>
                     <li class="info-li">브랜드 : ${stationDetailInfo.POLL_DIV_CO}</li>
                     <li class="info-li">전화번호 : ${stationDetailInfo.TEL} </li>
                     <li class="info-li">세차장 : ${stationDetailInfo.CAR_WASH_YN} </li>
                     <li class="info-li">휘발유 : ${stationDetailInfo.B027} </li>
                     <li class="info-li">경유 : ${stationDetailInfo.D047} </li>
                     <li class="info-li">고급휘발유 : ${stationDetailInfo.B034} </li>
                 </ul>
                 <div class="manage-buttons">
                     <button class="manage-button link-primary">찜하기</button>
                     <button class="manage-button link-primary">가격알림</button>
                 </div>
             </div>
        </div>
         `;

        mapOverlay = new kakao.maps.CustomOverlay({
            position: overlayLocation,
            content: overlayContent
        });
        mapOverlay.setMap(map);
        addMarker(null);
        markerArr=[];
        markerArr.push(marker);
        addMarker(map);
    },

    //지도 오버레이창 끄기
    closeOverlay:function(){
        mapOverlay.setMap(null);
    },

    //주유소 상세정보에서 유가 정보는 따로 빼서 객체에 넣기
    addOilPriceInStationDetailInfo:function(stationDetailInfo){
        Object.assign(stationDetailInfo,{B027:'',D047:'',B034:''});
        for(var i=0;i<stationDetailInfo.OIL_PRICE.length;i++){
            if(stationDetailInfo.OIL_PRICE[i].PRODCD =='B027'){
                stationDetailInfo.B027 = stationDetailInfo.OIL_PRICE[i].PRICE;
            }else if(stationDetailInfo.OIL_PRICE[i].PRODCD =='D047'){
                stationDetailInfo.D047 = stationDetailInfo.OIL_PRICE[i].PRICE;
            }else if(stationDetailInfo.OIL_PRICE[i].PRODCD =='B034'){
                stationDetailInfo.B034 = stationDetailInfo.OIL_PRICE[i].PRICE;
            }
        }
        return stationDetailInfo;
    },

}

