# ChatBotChum
Версия 1.7

Авторы: Харитонов Сергей и Цуп Илья

## Описание
Данное приложение реализует Chefot'а - бота Шефа, который может помочь выбрать блюдо на вечер, составить блюдо из имеющихся ингредиентов, или просто развить своё знание о кулинарии. Приложение поддерживает 2 режима работы: консольный, и Telegram.


## Состав
* Реализации баз данных: `Databases/`
* Интерфейсы: `Interfaces/`
* Основные модули: `Core/`
* Тесты: `Tests/`


## Базы данных
Имеется поддержка двух типов баз данных:
* 1) JSON
В этом случае файлы в формате JSON загружаются в память с диска по мере надобности.
* 2) MySQL
В этом случае информация передается через SQL запросы.


## Подробности реализации
Модуль, реализующий работу с контекстом пользователя, основан на персистентной структуре данных, которая, в свою очередь, основана на самобалансирующихся деревьях, где в узлах хранится int хэш объектов. Благодаря этой структуре, можно эффективно отслеживать историю поиска рецепта, и возвращаться к любой итерации без серьёзных потерь по производительности.

