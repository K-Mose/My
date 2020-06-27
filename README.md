# What to eat today

what to eat today, **오늘 뭐 먹지**는 데이터 크롤링 분석을 통한 식당 랭킹 어플입니다. <br/>
오늘 뭐 먹지는 Python의 BeautifulSoap4 라이브러리를 사용하여서 크롤링한 네이버 블로그 데이터들을 customized-KoNLPy를 이용해 전처리 하고 
특정 태그에 따라 TFIDF와 Word2Vec 으로 점수를 내서, 점수와 카테고리별로 순위를 나누는 어플입니다. <br/>

### Tag
 Cafe | Restaurant 
 -----|-----------
  잘하는 (well) | 잘하는 (well)
  감각적 (sensible) | 혼밥 (solo)
  가성비 (cost_effect) | 가성비 (cost_effect)
  맛있는 (tasty) | 맛있는 (tasty)
  분위기 (atmosphere) | 분위기 (atmosphere) 
  데이트 (dating) | 데이트 (dating)

## Preview
Cafe | Restaurant | Searching
----|----|-----
<img src="https://user-images.githubusercontent.com/55622345/85929206-de9fdc80-b8ed-11ea-9f28-56fc5e7a5754.jpg"  width="200" height="390"> | <img src="https://user-images.githubusercontent.com/55622345/85929212-ea8b9e80-b8ed-11ea-95af-fec9a3043400.jpg"  width="200" height="390"> | <img src="https://user-images.githubusercontent.com/55622345/85929435-caf57580-b8ef-11ea-9dd6-7eaac6041862.jpg"  width="200" height="390">

MapView(one) | MapView(branches)
----|----
<img src="https://user-images.githubusercontent.com/55622345/85929464-1f98f080-b8f0-11ea-87de-df15ada7f065.jpg"  width="200" height="390"> | <img src="https://user-images.githubusercontent.com/55622345/85929477-40614600-b8f0-11ea-9250-c2a0069ee819.jpg"  width="200" height="390">

<br/>


## App Structure
<img src="https://user-images.githubusercontent.com/55622345/85928480-3e938480-b8e8-11ea-870e-e280214e6ca5.png" width="600" height="660">

## Used
* Kotlin 1.3.41
* Kakao Map Android API
* Python 
  * BeautifulSoup4
  * customized-KoNLPy
* MySQL
  * JDBC driver 
