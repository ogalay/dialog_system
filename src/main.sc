require: functions.js

  module = sys.zb-common
theme: /

    state: /hello
        q!: $regex</start>
        q!: * (привет/здравствуй*/~добрый (~утро/~день/~вечер/~ночь)) *
        intent!: /Приветствие
        a: Привет-привет

    state: /weather
        q!: * (~погода/~температура/~градус)*
        intent!: /geo
        script:
            var city = $caila.inflect($parseTree._geo, ["nomn"]);
            openWeatherMapCurrent("metric", "ru", city).then(function (res) {
                if (res && res.weather) {
                    $reactions.answer("Сегодня в городе " + capitalize(city) + " " + res.weather[0].description + ", " + Math.round(res.main.temp) + "°C" );
                    if(res.weather[0].main == 'Rain' || res.weather[0].main == 'Drizzle') {
                        $reactions.answer("Советую захватить с собой зонтик!")
                    } else if (Math.round(res.main.temp) < 0) {
                        $reactions.answer("Бррррр ну и мороз")
                    }
                } else {
                    $reactions.answer("Что-то сервер барахлит. Не могу узнать погоду.");
                }
            }).catch(function (err) {
                $reactions.answer("Что-то сервер барахлит. Не могу узнать погоду.");
            });

    state: /currency
        intent!: /currency
        a: Я не понял. Вы сказали: {{$request.query}}

    state: /NoMatch
        event!: noMatch
        random: 
            a: Я не понял. Вы сказали: {{$request.query}}
            a: Извините, я не понял, что вы сказали