package softserve.academy.mylist

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class MyListTest {

    // Створюємо правило для тестування, яке ініціалізує MainActivity.
    // Це дозволяє тесту взаємодіяти з реальним контекстом додатка.
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAddItem() {
        // Генеруємо унікальну назву елемента з міткою часу, щоб уникнути
        // конфліктів у базі даних при повторних запусках тестів.
        val itemName = "Apple_${System.currentTimeMillis()}"

        // Знаходимо текстове поле за тегом, очищуємо його від попереднього змісту
        // та симулюємо введення тексту користувачем.
        composeTestRule.onNodeWithTag("item_input").performTextClearance()
        composeTestRule.onNodeWithTag("item_input").performTextInput(itemName)

        // Виконуємо натискання на кнопку додавання для запуску бізнес-логіки.
        composeTestRule.onNodeWithTag("add_button").performClick()

        // Очікуємо завершення асинхронних операцій у ViewModel та Room,
        // щоб графічний інтерфейс перейшов у стан спокою та оновив список.
        composeTestRule.waitForIdle()

        // Перевіряємо, чи відображається новий вузол з вказаним текстом на екрані.
        composeTestRule.onNodeWithText(itemName).assertIsDisplayed()
    }

    @Test
    fun testDeleteItem() {
        val itemName = "DeleteMe_${System.currentTimeMillis()}"

        // Підготовчий етап: створюємо елемент, який підлягає видаленню.
        composeTestRule.onNodeWithTag("item_input").performTextInput(itemName)
        composeTestRule.onNodeWithTag("add_button").performClick()
        composeTestRule.waitForIdle()

        // Шукаємо всі кнопки видалення за тегом. Обираємо першу знайдена (onFirst),
        // щоб видалити щойно доданий запис.
        composeTestRule.onAllNodesWithTag("delete_button").onFirst().performClick()

        // Чекаємо, поки операція видалення в БД завершиться і елемент зникне з UI.
        composeTestRule.waitForIdle()

        // Переконуємося, що елемент із заданою назвою більше не існує в дереві компонентів.
        composeTestRule.onNodeWithText(itemName).assertDoesNotExist()
    }

    @Test
    fun testDataPersistence() {
        val itemName = "PersistentItem_${System.currentTimeMillis()}"

        // Симулюємо повний цикл додавання об'єкта.
        composeTestRule.onNodeWithTag("item_input").performTextInput(itemName)
        composeTestRule.onNodeWithTag("add_button").performClick()

        // Очікуємо синхронізації стану додатка.
        composeTestRule.waitForIdle()

        // Перевіряємо, що дані успішно пройшли крізь ViewModel та відображаються у списку.
        composeTestRule.onNodeWithText(itemName).assertIsDisplayed()
    }
}