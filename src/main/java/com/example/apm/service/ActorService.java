package com.example.apm.service;

import com.example.apm.DataNotFoundException;
import com.example.apm.entity.Actor;
import com.example.apm.entity.SiteUser;
import com.example.apm.entity.Theater;
import com.example.apm.repository.ActorRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
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


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
@EnableScheduling
public class ActorService {
    private final ActorRepository actorRepository;

    public Page<Actor> getActorList(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    public Actor getActor(Integer actorId) {
        Optional<Actor> actor = this.actorRepository.findByActorId(actorId);
        if (actor.isPresent()) {
            return actor.get();
        } else {
            throw new DataNotFoundException("배우 정보가 없습니다.");
        }
    }

    @Scheduled(cron = "0 4 1/15 * * *") // 매월 15일 새벽 4시에 크롤링
    public void scheduled(){
        List<String[]> crollingData = actorDataCrolling();
        dataSave(crollingData);
    }

    public void dataSave(List<String[]> crollingData){
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
            driver.get("http://www.playdb.co.kr/artistdb/list.asp?code=013003");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("container1")));

            for (int i = 1; i < 394; i++) {

                // 페이지를 넘기는 구간
                try {
                    // 2페이지로 이동하는 작업 수행
                    ((JavascriptExecutor) driver).executeScript("listShow('013003', '', '" + i + "');");
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


}
