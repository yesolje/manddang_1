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

 // proj4js 라이브러리 불러오기
 // Katec 좌표
    proj4.defs("EPSG:5181", "+proj=tmerc +lat_0=38 +lon_0=128 +k=0.9999 +x_0=400000 +y_0=600000 +ellps=bessel +units=m +no_defs");
    proj4.defs("EPSG:4326", "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");


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
        table.on("rowClick", function(e, row){
            console.log(row.getData().GIS_X_COOR);
            console.log(row.getData().GIS_Y_COOR);
            var latlng = main.alterKatecToWgs(row.getData().GIS_X_COOR,row.getData().GIS_Y_COOR);
            main.setSenter(latlng[1],latlng[0]);

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
         	data:oilPriceData, //assign data to table
         	layout:"fitColumns", //fit columns to width of table (optional)
         	pagination:true, //enable.
            paginationSize:5, // this option can take any positive integer value
         	columns:[ //Define Table Columns
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

    setSenter:function(lat,lng){
    console.log("여기까지 들어왔어");
    console.log(lat);
    console.log(lng);
    var moveLatLon = new kakao.maps.LatLng(lat,lng);
    map.setCenter(moveLatLon);
    var marker = new kakao.maps.Marker({
        position: moveLatLon
    });
    marker.setMap(map);
    },

    alterKatecToWgs:function(x,y){
        var katecArr = [];
        katecArr[0] = x;
        katecArr[1] = y;
        var wgs84Coords = proj4("EPSG:5181", "EPSG:4326", katecArr);
        return wgs84Coords;
    },

}

