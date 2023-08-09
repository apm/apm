package com.example.apm.service;

import com.example.apm.entity.*;
import com.example.apm.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
@EnableScheduling
public class CrollingService {
    private final ActorRepository actorRepository;
    private final TheaterRepository theaterRepository;
    private final PmShowRepository pmShowRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;

    @PersistenceContext
    private EntityManager entityManager;


    // 배우정보 크롤링 파트
    @Scheduled(cron = "0 49 16 * * *") // 매월 15일 새벽 4시에 크롤링
    public void actorDataCrollingscheduled(){
        List<String[]> crollingData = actorDataCrolling();
        actorDataSave(crollingData);
    }

    public void actorDataSave(List<String[]> crollingData){
        for (String[] row : crollingData){
            Integer actorNumber = Integer.parseInt(row[0]);
            String actorImgSrc = row[1];
            String actorName = row[2];
            String actorJob = row[3];
            String actorRecentPlay1 = row[4];
            String actorRecentPlay2 = row[5];
            String actorRecentPlay3 = row[6];

            Actor actor = new Actor(actorNumber, actorImgSrc, actorName, actorJob,
                    actorRecentPlay1, actorRecentPlay2, actorRecentPlay3);

            actorRepository.save(actor);
        }
    }

    public List<String[]> actorDataCrolling() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\tjrqo\\Downloads\\chromedriver_win32 (2)\\chromedriver.exe");
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver();

        List<String[]> actorList = null;

        try {
            // 데이터 저장소 생성 및 웹페이지 접근
            actorList = new ArrayList<>();
            String[] actor;

            // 페이지 이동 대기 시간과 대기 간격 설정
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // 웹 페이지 접근
            driver.get("http://www.playdb.co.kr/artistdb/list.asp?code=013014");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("container1")));

            for (int i = 1; i < 33; i++) {

                // 페이지를 넘기는 구간
                try {
                    // 2페이지로 이동하는 작업 수행
                    ((JavascriptExecutor) driver).executeScript("listShow('013014', '', '" + i + "');");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.className("container1")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                WebElement iframeElement = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/table/tbody/tr[4]/td/div[1]/iframe"));
                driver.switchTo().frame(iframeElement);
                // 해당 페이지에서 데이터를 추출하는 부분
                for (int count = 1; count <= 80; count++) {
                    if (count % 4 == 3) {
                        actor = new String[7];

                        // 배우번호 추출
                        WebElement actorNumber = driver.findElement(By.xpath("/html/body/table/tbody/tr[" + count + "]/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/a"));
                        actor[0] = actorNumber.getAttribute("href");
                        int anStart = actor[0].indexOf("ManNo=") + 6;
                        int anEnd = actor[0].indexOf("&part");
                        actor[0] = actor[0].substring(anStart, anEnd);

                        // 배우사진링크
                        WebElement actorImgSrc = driver.findElement(By.xpath("/html/body/table/tbody/tr[" + count + "]/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/a/img"));
                        actor[1] = actorImgSrc.getAttribute("src");

                        // 한글이름
                        WebElement actorName = driver.findElement(By.xpath("/html/body/table/tbody/tr[" + count + "]/td/table/tbody/tr/td[1]/table/tbody/tr/td[2]/a"));
                        actor[2] = actorName.getText();

                        // 직업 1~3 (보충작업 필요함)
                        WebElement actorJob = driver.findElement(By.xpath("/html/body/table/tbody/tr[" + count + "]/td/table/tbody/tr/td[3]"));
                        actor[3] = actorJob.getText();

                        // 최근공연1,2,3
                        List<WebElement> recentPlay = driver.findElements(By.xpath("/html/body/table/tbody/tr[" + count + "]/td/table/tbody/tr/td[5]/a"));
                        int j = 4;
                        for (WebElement data : recentPlay) {
                            actor[j++] = data.getText();
                        }
                        actorList.add(actor);
                    }
                }
                log.info(i+ "Page Complete");
                driver.switchTo().defaultContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        return actorList;
    }
    // 좌석 데이터 저장 파트
    @Scheduled(cron="0 31 0 * * *")
    public void seatSchedule() throws IOException{
        log.info("Seat Save Start");
        List<Theater> theaters = theaterRepository.findAll();
        List<PmShow> pmShows = pmShowRepository.findAll();

        Set<Integer> uniqueTheaterIds = new HashSet<>();
        for (PmShow pmShow : pmShows) {
            uniqueTheaterIds.add(pmShow.getTheater().getTheaterId());
        }

        for (Theater theater : theaters) {
            if (uniqueTheaterIds.contains(theater.getTheaterId())) {
                System.out.println(theater.getTheaterId());
                for (int i=1; i<=theater.getTheaterTotalSeat(); i++){
                    Seat seat = new Seat();
                    seat.setSeatName("Seat" + i);
                    seat.setTheater(theater);
                    this.seatRepository.save(seat);
                }
            }
        }
        System.out.println("End");

//        for (Theater row : theaters){
//            rel = 0;
//            if (row.getTheaterId().intValue() == temp[rel++]){
//                int totalSeat = row.getTheaterTotalSeat();
//                log.info("before save");
//                for (int i=1; i<=totalSeat; i++){
//                    log.info("row == pmshow");
//                    Seat seat = new Seat();
//                    seat.setSeatName("Seat" + i);
//                    seat.setTheater(row);
//                    System.out.println("Comp");
//                    this.seatRepository.save(seat);
//                }
//            }
//                    log.info("rest time");
//        }

    }


    // 극장 데이터 크롤링 파트
    @Scheduled(cron = "0 46 14 * * *") // 매월 15일 새벽 4시에 크롤링
    public void scheduled() throws IOException {
        List<String[]> crollingData = theaterDataCrolling();
        theaterDataSave(crollingData);
    }

    public void theaterDataSave(List<String[]> theaterDataCrolling) {

        for (String[] row : theaterDataCrolling) {
            try{
                Integer theaterNumber = Integer.parseInt(row[0]);
                String theaterName = row[1];
                String theaterAddress = row[2];
                String theaterPhoneNumber = row[3];
                String theaterHomePage = row[4];
                Integer theaterSeat = Integer.parseInt(row[5]);

                List<Seat> seatList = new ArrayList<>();

                Theater theaterinput = new Theater();
                theaterinput.setTheaterId(theaterNumber);
                theaterinput.setTheaterName(theaterName);

                for (int a = 1; a <= theaterSeat; a++) {
                    Seat seat = new Seat();
                    seat.setSeatName("좌석" + a);
                    seat.setTheater(theaterinput);
                    seatList.add(seat); // seatList에 좌석 정보 추가
                    this.seatRepository.save(seat);
                }

                theaterinput.setSeatList(seatList); // theaterinput의 seatList 설정

                theaterinput.setTheaterLocation(theaterAddress);
                theaterinput.setTheaterPhoneNumber(theaterPhoneNumber);
                theaterinput.setTheaterHomePage(theaterHomePage);
                theaterinput.setTheaterTotalSeat(theaterSeat);

                this.theaterRepository.save(theaterinput); // theaterinput 저장
            }catch (Exception e){
                log.info(e.toString());
            }finally {
                continue;
            }

        }
    }

    public List<String[]> theaterDataCrolling() throws IOException {

        List<String[]> theaterData = new ArrayList<>();
        for (int i = 1; i <=223; i++) {
            String url = "http://www.playdb.co.kr/placedb/PlacedbList.asp?Page=" + i + "&strTab=1&selRegion=&selSubRegion=";
            Connection conn = Jsoup.connect(url);
            Document doc = conn.get();


            String[] list = new String[20];
            String[] number = new String[20];
            String[] data;
            for (int j = 4; j < 81; j += 4) {
                String tempUrl = new String();

                String getHref = "#contents > div:nth-child(2) > table > tbody > tr:nth-child(4) > td > table > tbody " +
                        "> tr:nth-child(" + j + ") > td > table > tbody > tr > td:nth-child(1) > table > tbody > tr > td > a";

                Element temp = null;
                temp = doc.selectFirst(getHref);
                tempUrl = temp.attr("href");
                list[(j/4)-1] = "http://www.playdb.co.kr/placedb/" + tempUrl;
            }
            for (int y=4; y<81; y+=4){
                String modifiedXPath = "#contents > div:nth-child(2) > table > tbody > " +
                        "tr:nth-child(4) > td > table > tbody > tr:nth-child(" + y + ") > td > " +
                        "table > tbody > tr > td:nth-child(1) > table > tbody > tr > td > a";

                String temp = doc.selectFirst(modifiedXPath).attr("href");
                int sIndex = temp.indexOf("PlacecCD=") + 9;
                number[y/4-1] = temp.substring(sIndex);
            }

            for (int k=0; k<20; k++){
                data = new String[6];
                conn = Jsoup.connect(list[k]);
                doc = conn.get();

                //*[@id="Keyword"]/table/tbody/tr[1]/td/table/tbody/tr/td[4]/b/font
                String theaterName = "#Keyword > table > tbody > tr:nth-child(1) > td > table > tbody > tr > td:nth-child(4) > b > font";

                String theaterAddress = "#Keyword > table > tbody > tr:nth-child(3) > td:nth-child(3) > table > tbody > tr:nth-child(1) > " +
                        "td > table > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(2) > td";
                String theaterPhoneNumber = "#Keyword > table > tbody > tr:nth-child(3) > td:nth-child(3) > table > tbody > tr:nth-child(1) > " +
                        "td > table > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(3) > td";
                //*[@id="Keyword"]/table/tbody/tr[3]/td[3]/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[4]/td/a
                String theaterHomePage = "#Keyword > table > tbody > tr:nth-child(3) > td:nth-child(3) > table > tbody > tr:nth-child(1) > " +
                        "td > table > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(4) > td > a";
                String theaterSeat = "#Tab0 > tbody > tr:nth-child(1) > td > table > tbody > tr:nth-child(2) > td > table > tbody > tr:nth-child(1) > td";

                data[0] = number[k];

                data[1] = doc.selectFirst(theaterName).text();

                data[2] = doc.selectFirst(theaterAddress).text();
                data[2] = data[2].replace("주소: ", "");

                data[3] = doc.selectFirst(theaterPhoneNumber).text();
                data[3] = data[3].replace("연락처: ", "");

                try{
                    data[4] = doc.selectFirst(theaterHomePage).text();
                }catch (Exception e){
                    log.info("Theater Homepage Null");
                    data[4] = "";
                }

                try{
                    data[5] = doc.selectFirst(theaterSeat).text();
                    String[] parts = data[5].split(" : ");
                    String seatCount = parts[1].replaceAll("석", "");
                    data[5] = seatCount;
                }catch (Exception e){
                    log.info("Theater Seat Info None");
                    data[5] = "1";
                }

                log.info(""+i+k);
                theaterData.add(data);
            }
        }
        Set<String[]> dataSet = new HashSet<>(theaterData);


        List<String[]> dataListWithoutDuplicates = new ArrayList<>(dataSet);

        return dataListWithoutDuplicates;
    }

    // 공연 및 스케쥴 크롤링 파트
    static PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    @Scheduled(cron = "0 29 22 * * *")
    public void schedule(){
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\tjrqo\\Downloads\\chromedriver_win32 (2)\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver();

        List<String> basicData = basicMusicData(driver);
        List<String> extractData = extractData(basicData);
        List<List<String[]>> detailData = detailData(extractData, driver);
        // printdetailData(detailData, out);


        for (String[] row : detailData.get(0)){
            String title = row[0];
            String theaterName = row[1].replace("(자세히)", "");
            String showDate= row[2]; //공연기간
            String runningTime= row[3]; //러닝타임
            String viewingAge= row[4];
            String otherNotice = row[5];
            String castingActor= row[6].replace("null", "");
            String castingName= row[7].replace("null", "");
            String castingInformation= row[8].replace("null", "");
            String notice1= row[9];
            String notice2= row[10];
            String saleinfo1= row[11];
            String saleinfo2= row[12];
            String playinfo1= row[13];
            String playinfo2= row[14];
            String castinginfo1= row[15];
            String castinginfo2= row[16];
            String genre = "연극";
            String synopsis = "";
            String pmShowId = row[17];
            String poster = row[18];
            PmShow pmShow = new PmShow(pmShowId,title, theaterName, showDate,
                    runningTime, viewingAge, otherNotice, castingActor,
                    castingName, castingInformation, notice1, notice2,
                    saleinfo1, saleinfo2, playinfo1, playinfo2, castinginfo1,
                    castinginfo2, genre, synopsis, poster);
            this.pmShowRepository.save(pmShow);

        }
        for (String[] row : detailData.get(1)){
            try{
                Integer actorId = Integer.parseInt(row[3]);
                String pmShowId = row[0];
                String date = row[1];
                String time = row[2];
                Actor actor = entityManager.find(Actor.class, actorId);
                PmShow pmShow = entityManager.find(PmShow.class, pmShowId);
                Schedule schedule = new Schedule(actor, pmShow, date, time);
                this.scheduleRepository.save(schedule);
            }catch (Exception e){

            }finally {
                continue;
            }

        }
    }

    public static List<String> basicMusicData(WebDriver driver) {

        List<String> dataList = null;
        try {
            driver.get("https://ticket.interpark.com/TPGoodsList.asp?Ca=Dra");

            // 데이터를 담을 리스트 생성
            dataList = new ArrayList<>();

            // 데이터 테이블 요소 선택
            WebElement dataTable = driver.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[3]/div/div/div[2]/div/table/tbody"));

            // 테이블 내부의 모든 tr 요소들을 가져오기
            List<WebElement> rows = dataTable.findElements(By.tagName("tr"));

            // 각 행(tr) 요소들 순회하며 데이터 추출
            for (WebElement row : rows) {
                // 행(tr) 안의 모든 셀(td) 요소들을 가져오기 (Xpath를 사용하여 td 요소 선택)
                List<WebElement> cells = row.findElements(By.xpath(".//td[1]"));

                // 첫 번째 셀(td) 요소의 텍스트를 추출하여 리스트에 추가
                if (!cells.isEmpty()) {
                    String link = cells.get(0).findElement(By.tagName("a")).getAttribute("href");
                    dataList.add(link);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static List<String> extractData(List<String> dataList){
        for (int i=0; i<dataList.size(); i++){
            String originalData = dataList.get(i);
            int start = originalData.indexOf("GroupCode=")+10;
            originalData = "https://tickets.interpark.com/goods/"
                    + originalData.substring(start);
            dataList.set(i, originalData);
        }
        return dataList;
    }

    public static void printBasicData(List<String> dataList, PrintStream out){
        // 가져온 데이터 출력
        for (String rowData : dataList) {
            out.println(rowData);
        }
    }
    public static void printdetailData(List<String[]> dataList, PrintStream out){
        // 가져온 데이터 출력
        for (String[] data : dataList) {
            for (String item : data) {
                out.println(item + " ");
            }
            out.println(); // 다음 줄로 넘어감
        }
    }

    public static List<List<String[]>> detailData(List<String> dataList, WebDriver driver){
        List<String[]> detailDataList = new ArrayList<>();
        List<String[]> scheduleList = new ArrayList<>();
        List<String[]> actorInfor = new ArrayList<>();
        String[] temp = new String[16];
        int count = 0;
        int i=0;
        for (String basicdata : dataList) {
            try {


                System.out.println(count++);
                driver.get(basicdata);

                temp = new String[19];
                // "더보기" 버튼 클릭
                // "더보기" 버튼 클릭
                WebElement moreButton = null;
                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // 최대 10초 동안 대기합니다.
                    moreButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("contentToggleBtn")));
                    Thread.sleep(500);
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", moreButton);
                } catch (Exception e) {
                    System.out.println("none js contentToggleBtn");
                }


                // 제목
                temp[0] = driver.findElement(By.xpath("//*[@id=\"container\"]/div[5]/div[1]/div[2]/div[1]/div/div[1]/h2")).getText();
                out.println("제목 : " + temp[0]);
                // 장소
                temp[1] = driver.findElement(By.xpath("//*[@id=\"container\"]/div[5]/div[1]/div[2]/div[1]/div/div[2]/ul/li[1]/div/a")).getAttribute("textContent");
                out.println("장소 : " + temp[1]);
                // 공연기간
                temp[2] = driver.findElement(By.xpath("//*[@id=\"container\"]/div[5]/div[1]/div[2]/div[1]/div/div[2]/ul/li[2]/div/p")).getText();
                out.println("공연기간 : " + temp[2]);
                // 공연시간
                temp[3] = driver.findElement(By.xpath("//*[@id=\"container\"]/div[5]/div[1]/div[2]/div[1]/div/div[2]/ul/li[3]/div/p")).getText();
                out.println("공연시간 : " + temp[3]);
                // 관람연령
                temp[4] = driver.findElement(By.xpath("//*[@id=\"container\"]/div[5]/div[1]/div[2]/div[1]/div/div[2]/ul/li[4]/div/p")).getText();
                out.println("관람연령 : " + temp[4]);
                // 캐스팅 관련 기타 공지사항(배우 변경 등)
                try {
                    temp[5] = driver.findElement(By.cssSelector(".content casting div.contentDetailList")).getText();
                } catch (Exception e) {
                    System.out.println("none contentDetailList");
                    temp[5] = null;
                }//
                out.println("기타사항 : " + temp[5]);

                String actorNumber = null;
                String castingActor = null;
                String castingName = null;
                String[] schedule;


                List<WebElement> actorList = driver.findElements(By.cssSelector(".content.casting .castingList .castingItem"));
                for (WebElement element : actorList) {
                    // 배우번호
                    schedule = new String[3];
                    actorNumber = element.findElement(By.cssSelector(".castingLink")).getAttribute("href");
                    int numberStart = actorNumber.indexOf("ManNo=") + 6;
                    actorNumber = actorNumber.substring(numberStart).replace("null", "");
                    // 역할이름
                    castingActor = element.findElement(By.cssSelector(".castingActor")).getText().replace("null", "");
                    // 배우이름
                    castingName = element.findElement(By.cssSelector(".castingName")).getText().replace("null", "");
                    schedule[0] = actorNumber;
                    schedule[1] = castingActor;
                    schedule[2] = castingName;

                    actorInfor.add(schedule);
                    temp[6] += actorNumber + "\t";
                    temp[7] += castingActor + "\t";
                    temp[8] += castingName + "\t";
                }
                try {
                    temp[6].replace("null", "");
                    temp[7].replace("null", "");
                    temp[8].replace("null", "");
                } catch (Exception e) {
                    temp[6] = "";
                    temp[7] = "";
                    temp[8] = "";
                }


                out.println("배우번호 : " + temp[6]);
                out.println("역할명 : " + temp[7]);
                out.println("배우명 : " + temp[8]);

                // 9-10까지 공지사항
                List<WebElement> noticeImgList = driver.findElements(By.xpath("//*[@id=\"productMainBody\"]/div/div/div[3]/div/img"));
                i = 9;
                for (WebElement element : noticeImgList) {

                    temp[i++] = element.getAttribute("src");
                }
                out.println("공지1 : " + temp[9]);
                out.println("공지2 : " + temp[10]);

                // 11~12까지 할인정보
                List<WebElement> saleImgList = driver.findElements(By.xpath("//*[@id=\"productMainBody\"]/div/div/div[4]/div/img"));
                i = 11;
                for (WebElement element : saleImgList) {

                    temp[i++] = element.getAttribute("src");
                }
                out.println("할인1 : " + temp[11]);
                out.println("할인2 : " + temp[12]);

                // 13~14 공연정보
                List<WebElement> informationImgList = driver.findElements(By.xpath("//*[@id=\"productMainBody\"]/div/div/div[5]/div/p[1]/strong/img"));
                i = 13;
                for (WebElement element : informationImgList) {
                    temp[i++] = element.getAttribute("src");
                }
                out.println("공연정보1 : " + temp[13]);
                out.println("공연정보2 : " + temp[14]);

                // 15~16 캐스팅 정보
                List<WebElement> castingImgList = driver.findElements(By.xpath("//*[@id=\"productMainBody\"]/div/div/div[5]/div/p[2]/strong/img"));
                i = 15;
                for (WebElement element : castingImgList) {
                    temp[i++] = element.getAttribute("src");
                }
                out.println("캐스팅정보1 : " + temp[15]);
                out.println("캐스팅정보2 : " + temp[16]);

                WebElement poster = driver.findElement(By.xpath("/html/body/div[1]/div[5]/div[1]/div[2]/div[1]/div/div[2]/div/div[1]/img"));
                temp[18] = poster.getAttribute("src");
                out.println("이미지 정보 : " + temp[18]);


                // 자바스크립트로 클릭할 버튼 요소를 찾아옵니다.
                WebElement buttonElement = driver.findElement(By.xpath("/html/body/div[1]/div[5]/div[1]/div[2]/div[2]/nav/div/div/ul/li[2]/a"));

                // 자바스크립트 실행을 위한 JavascriptExecutor 객체를 생성합니다.
                JavascriptExecutor executor = (JavascriptExecutor) driver;

                try {
                    executor.executeScript("arguments[0].click();", buttonElement);
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[5]/div[1]/div[2]/div[2]/div/div/div[3]/table/tbody")));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 자바스크립트를 사용하여 버튼을 클릭합니다.

                String[] casting = null;
                WebElement tbodyInfo = driver.findElement(By.xpath("/html/body/div[1]/div[5]/div[1]/div[2]/div[2]/div/div/div[3]/table/tbody"));
                List<WebElement> trElements = tbodyInfo.findElements(By.tagName("tr"));

                int castingInfoCount = 0;
                for (WebElement row1 : trElements) {
                    if (castingInfoCount == 0) {
                        castingInfoCount++;
                        continue;
                    } else {
                        List<WebElement> tdElements = row1.findElements(By.tagName("td"));
                        String Date = null;
                        String Time = null;
                        int skip = 1;
                        for (WebElement row2 : tdElements) {
                            casting = new String[4];
                            int playNo = basicdata.indexOf("goods/") + 6;
                            casting[0] = temp[17] = basicdata.substring(playNo);
                            if (skip == 1) {
                                Date = row2.getText();
                                skip++;
                            } else if (skip == 2) {
                                Time = row2.getText();
                                skip++;
                            } else {
                                casting[1] = Date;
                                casting[2] = Time;
                                casting[3] = row2.getText();

                                for (String[] array : actorInfor) {
                                    if (array[2].equals(casting[3])) {
                                        casting[3] = array[0];
                                        break;
                                    }
                                }
                                scheduleList.add(casting);
                            }

                        }
                        // 저장하는 코드

                    }
                    castingInfoCount++;
                }


                detailDataList.add(temp);

            }catch (Exception e){
                System.out.print(count);
                e.printStackTrace();
            }finally {
                continue;
            }
        }

        return new ArrayList<>(List.of(detailDataList, scheduleList));
    }


}
