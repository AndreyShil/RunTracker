# О приложении

Приложение "RunTracker" предназначено для трекинга беговых тренировок.

---

# Возможности приложения

### 1. Логин/регистрация пользователя:

* Осуществление авторизации пользователя по логину и паролю
* Возможность зарегистрироваться новым пользователям, указав основную информацию

<p align="center">
    <img src="/art/screenshot_sign_in_light.jpg" alt="Экран авторизации (светлая тема)" height="700" hspace="10">
    <img src="/art/screenshot_sign_in_dark.jpg" alt="Экран авторизации (темная тема)" height="700" hspace="10" >
</p>

<p align="center">
    <img src="/art/screenshot_sign_up_light.jpg" alt="Экран регистрации (светлая тема)" height="700" hspace="10">
    <img src="/art/screenshot_sign_up_dark.jpg" alt="Экран регистрации (темная тема)" height="700" hspace="10" >
</p>

### 2. История тренировок:

* Отображение списка с историей беговых тренировок с указанием основной информации: расстояние,
  время, калории
* Возможность увеличить элемента списка для более детального показа трека тренировки

<p align="center">
    <img src="/art/screenshot_races_light.jpg" alt="Экран со списком тренировок (светлая тема)" height="700" hspace="10">
    <img src="/art/screenshot_races_dark.jpg" alt="Экран со списком тренировок (темная тема)" height="700" hspace="10" >
</p>

### 3. Добавление новой тренировки:

* Возможность начать новую беговую тренировку
* При необходимости начатую тренировку можно приостановить или закончить
* Отображение уведомления о текущей тренировки

<p align="center">
    <img src="/art/screenshot_tracker_light.jpg" alt="Экран старта новой тренировки (светлая тема)" height="700" hspace="10">
    <img src="/art/screenshot_tracker_dark.jpg" alt="Экран старта новой тренировки (темная тема)" height="700" hspace="10" >
</p>

### 4. Отображение статистики тренировок:

* Просмотр статистики пройденного расстояния
* Просмотр статистики потраченных калорий
* Просмотр статистики средней скорости
* Отображение перечисленных характеристик, а также их средних показателей за определенный промежуток
  времени (последняя неделя, 1 месяц или 3 месяца)

<p align="center">
    <img src="/art/screenshot_stats_light.jpg" alt="Экран статистики (светлая тема)" height="700" hspace="10">
    <img src="/art/screenshot_stats_dark.jpg" alt="Экран статистики (темная тема)" height="700" hspace="10" >
</p>

### 5. Профиль пользователя:

* Отображение основной информации о пользователе
* Возможность обновить аватар пользователя
* Возможность выбрать тему приложения (светлая, темная или как в системе)
* Просмотр активных сессий с возможностью удалить определенные сессии
* Возможность выйти из профиля

<p align="center">
    <img src="/art/screenshot_profile_light.jpg" alt="Экран профиля (светлая тема)" height="700" hspace="10">
    <img src="/art/screenshot_profile_dark.jpg" alt="Экран профиля (темная тема)" height="700" hspace="10" >
</p>

---

# Основной стек технологий

* Kotlin + Coroutines + Flow
* Clean Architecture + MVI
* Multimodule Gradle Project
* Material 3
* DI - Dagger 2
* Network - OkHttp + Retrofit
* Database - Room Persistence Library
* Remote storage - Firebase storage
* Image display - Coil
* Unit testing - JUnit, Mockito, Robolectric
* UI testing - Espresso

---

### Структура Gradle-модулей

![Схема зависимостей градл модулей](art/diagram_modules.png)

### Структура Dagger-компонентов

![Схема зависимостей Dagger компонентов](art/diagram_dagger_components.png)

---

Logo image by pikisuperstar
on [Freepik](https://www.freepik.com/free-vector/abstract-runner-silhouette-flat-design_4927829.htm#query=run%20logo&position=18&from_view=search&track=ais)

Image on profile screen by GarryKillian
on [Freepik](https://ru.freepik.com/free-vector/map-of-big-data-in-modern-city_8289177.htm#query=street%20map&position=13&from_view=search&track=ais)