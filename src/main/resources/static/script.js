document.addEventListener('DOMContentLoaded', () => {
    const addPurchaseForm = document.getElementById('addPurchaseForm');
    const purchasesListElement = document.getElementById('purchasesList');
    const titleInput = document.getElementById('title');
    const quantityInput = document.getElementById('quantity');
    const errorMessageElement = document.getElementById('error-message');
    const loadingMessageElement = document.getElementById('loading-message');

    const API_BASE_URL = '/purchases'; // Убедитесь, что путь соответствует вашему контроллеру

    // Функция для отображения ошибок
    function displayError(message) {
        errorMessageElement.textContent = message;
        setTimeout(() => {
            errorMessageElement.textContent = ''; // Очистить сообщение через некоторое время
        }, 5000);
    }

    // Функция для получения и отображения списка покупок
    async function fetchAndDisplayPurchases() {
        loadingMessageElement.style.display = 'block';
        purchasesListElement.innerHTML = ''; // Очищаем старый список

        try {
            const response = await fetch(API_BASE_URL);
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ error: `Ошибка сервера: ${response.status}` }));
                throw new Error(errorData.error || `HTTP error! status: ${response.status}`);
            }
            const purchases = await response.json();

            if (purchases.length === 0) {
                purchasesListElement.innerHTML = '<li>Список покупок пуст.</li>';
            } else {
                purchases.forEach(purchase => {
                    const listItem = document.createElement('li');
                    listItem.className = purchase.status === 'BOUGHT' ? 'bought' : '';

                    const textNode = document.createTextNode(`${purchase.title} - ${purchase.quantity} шт. (Статус: ${purchase.status}) `);
                    listItem.appendChild(textNode);

                    if (purchase.status !== 'BOUGHT') {
                        const buyButton = document.createElement('button');
                        buyButton.textContent = 'Куплено';
                        buyButton.dataset.title = purchase.title; // Сохраняем title для запроса
                        buyButton.addEventListener('click', markAsBought);
                        listItem.appendChild(buyButton);
                    }
                    purchasesListElement.appendChild(listItem);
                });
            }
        } catch (error) {
            console.error('Ошибка при загрузке списка покупок:', error);
            displayError(`Не удалось загрузить список: ${error.message}`);
            purchasesListElement.innerHTML = '<li>Не удалось загрузить список покупок.</li>';
        } finally {
            loadingMessageElement.style.display = 'none';
        }
    }

    // Функция для добавления новой покупки
    async function addPurchase(event) {
        event.preventDefault(); // Предотвращаем стандартную отправку формы

        const title = titleInput.value.trim();
        const quantity = parseInt(quantityInput.value, 10);

        if (!title || quantity <= 0) {
            displayError('Пожалуйста, введите корректное название и количество.');
            return;
        }

        const purchaseData = {
            title: title,
            quantity: quantity
        };

        try {
            const response = await fetch(API_BASE_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(purchaseData),
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ error: `Ошибка сервера: ${response.status}` }));
                throw new Error(errorData.error || `HTTP error! status: ${response.status} - ${response.statusText}`);
            }

            // const result = await response.json(); // Можно использовать ответ, если он полезен
            // console.log('Покупка добавлена:', result);

            addPurchaseForm.reset(); // Очищаем форму
            fetchAndDisplayPurchases(); // Обновляем список
        } catch (error) {
            console.error('Ошибка при добавлении покупки:', error);
            displayError(`Не удалось добавить покупку: ${error.message}`);
        }
    }

    // Функция для отметки покупки как "куплено"
    async function markAsBought(event) {
        const titleToUpdate = event.target.dataset.title;

        if (!titleToUpdate) {
            displayError('Не удалось определить товар для обновления.');
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/${encodeURIComponent(titleToUpdate)}`, { // encodeURIComponent для title с пробелами и спец. символами
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json', // Хотя тело тут не нужно, заголовок может быть полезен
                },
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ error: `Ошибка сервера: ${response.status}` }));
                throw new Error(errorData.error || `HTTP error! status: ${response.status} - ${response.statusText}`);
            }

            // const result = await response.json(); // Можно использовать ответ, если он полезен
            // console.log('Статус обновлен:', result);

            fetchAndDisplayPurchases(); // Обновляем список
        } catch (error) {
            console.error('Ошибка при обновлении статуса:', error);
            displayError(`Не удалось обновить статус: ${error.message}`);
        }
    }

    // Инициализация: привязываем обработчик к форме и загружаем список
    if (addPurchaseForm) {
        addPurchaseForm.addEventListener('submit', addPurchase);
    }
    fetchAndDisplayPurchases();
});