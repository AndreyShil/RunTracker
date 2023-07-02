# О приложении

Приложение "RunTracker" предназначено для трекинга беговых тренировок.

---

# Возможности приложения

### 1. Логин/регистрация пользователя:

* Осуществление авторизации пользователя по логину и паролю
* Возможность зарегистрироваться новым пользователям, указав основную информацию

![Экран авторизации (светлая тема)](art/screenshot_sign_in_light.jpg)
![Экран авторизации (темная тема)](art/screenshot_sign_in_dark.jpg)

![Экран регистрации (светлая тема)](art/screenshot_sign_up_light.jpg)
![Экран регистрации (темная тема)](art/screenshot_sign_up_dark.jpg)

### 2. История тренировок:

* Отображение списка с историей беговых тренировок с указанием основной информации: расстояние,
  время, калории
* Возможность увеличить элемента списка для более детального показа трека тренировки

![Экран со списком тренировок (светлая тема)](art/screenshot_races_light.jpg)
![Экран со списком тренировок (темная тема)](art/screenshot_races_dark.jpg)

### 3. Добавление новой тренировки:

* Возможность начать новую беговую тренировку
* При необходимости начатую тренировку можно приостановить или закончить
* Отображение уведомления о текущей тренировки

![Экран старта новой тренировки (светлая тема)](art/screenshot_tracker_light.jpg)
![Экран старта новой тренировки (темная тема)](art/screenshot_tracker_dark.jpg)

### 4. Отображение статистики тренировок:

* Просмотр статистики пройденного расстояния
* Просмотр статистики потраченных калорий
* Просмотр статистики средней скорости
* Отображение перечисленных характеристик, а также их средних показателей за определенный промежуток
  времени (последняя неделя, 1 месяц или 3 месяца)

![Экран статистики (светлая тема)](art/screenshot_stats_light.jpg)
![Экран статистики (темная тема)](art/screenshot_stats_dark.jpg)

### 5. Профиль пользователя:

* Отображение основной информации о пользователе
* Возможность обновить аватар пользователя
* Возможность выбрать тему приложения (светлая, темная или как в системе)
* Просмотр активных сессий с возможностью удалить определенные сессии
* Возможность выйти из профиля

![Экран профиля (светлая тема)](art/screenshot_profile_light.jpg)
![Экран профиля (темная тема)](art/screenshot_profile_dark.jpg)

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

Logo image by pikisuperstar on [Freepik](https://www.freepik.com/free-vector/abstract-runner-silhouette-flat-design_4927829.htm#query=run%20logo&position=18&from_view=search&track=ais)

Image on profile screen by GarryKillian on [Freepik](https://ru.freepik.com/free-vector/map-of-big-data-in-modern-city_8289177.htm#query=street%20map&position=13&from_view=search&track=ais)