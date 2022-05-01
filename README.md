# MoviesRecycler
Тренировочный проект по показу данных от сервера https://developer.nytimes.com 
Архитектура MVVM.

Стек: kotlin, hilt, retrofit+okhttp, coil, splashscreen.

Функционал:
1. Splashscreen с анимацией(12 android) и ожидания первоначальной загрузки данных от сервера:

![splash_screen](https://user-images.githubusercontent.com/79571688/166143252-0a296777-140b-4f5f-8273-27730c5f43ea.gif)


2.  Фильмы отображаются RecyclerView в сетке, котoрая зависит от ширины экрана:
![2](https://user-images.githubusercontent.com/79571688/166143562-26a90df2-9c3a-4734-899e-e63bd332ceb5.png)

3. Pagination: данные от сервера приходят по 20 айтемов, и дозагружаются по мере скролла. На время загрузки показывается шиммер:

![pagination_shimmer_short](https://user-images.githubusercontent.com/79571688/166143805-1163cfd4-39ed-413b-95ab-a39d351aae8f.gif)

4. Поиск фильма по названию:

![search_movie](https://user-images.githubusercontent.com/79571688/166143897-dd72db9f-1ffb-4a90-8ed0-9dbd9449fad4.gif)

