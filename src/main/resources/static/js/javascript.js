document.addEventListener('DOMContentLoaded', function () {
    // 1) Dwupoziomowe menu – przełączanie sekcji (zmiana widoczności elementów)
    const sectionButtons = document.querySelectorAll('.sub-nav-link');
    const sections = document.querySelectorAll('.content-section');

    function showSection(targetId) {
        sections.forEach(sec => sec.classList.remove('is-visible'));
        const target = document.getElementById(targetId);
        if (target) {
            target.classList.add('is-visible');
        }

        sectionButtons.forEach(btn => btn.classList.remove('active'));
        sectionButtons.forEach(btn => {
            if (btn.dataset.target === targetId) {
                btn.classList.add('active');
            }
        });
    }

    sectionButtons.forEach(button => {
        button.addEventListener('click', () => {
            const targetId = button.dataset.target;
            showSection(targetId);
        });
    });

    const urlParam = new URLSearchParams(window.location.search);
    const isRegistered = urlParam.get("registered") === "1";
    // Ustaw sekcję domyślną w zależności od roli
    if (isRegistered)
    {const role = document.body.dataset.role;
        if (role === 'PATIENT') {
            showSection('section-patient');
        } else if (role === 'DOCTOR') {
            showSection('section-doctor');
        } else {
            showSection('section-dashboard');
        }
    }


    // 2) Kliknięcie wiersz tabeli – zmiana wyglądu elementu (dodanie klasy)
    const rows = document.querySelectorAll('.appointment-row');
    rows.forEach(row => {
        row.addEventListener('click', () => {
            row.classList.toggle('row-emphasis');
        });
    });

    // 3) Dynamiczne podsumowanie – zmiana struktury DOM i nowe informacje
    const statsList = document.getElementById('dynamic-stats');
    if (statsList) {
        let openCount = 0;
        let reservedCount = 0;
        let acceptedCount = 0;

        rows.forEach(row => {
            const status = row.dataset.status;
            if (status === 'Open') {
                openCount++;
            } else if (status === 'Reserved') {
                reservedCount++;
            } else if (status === 'Accepted') {
                acceptedCount++;
            }
        });

        statsList.innerHTML = '';

        const liOpen = document.createElement('li');
        liOpen.textContent = 'Wolne terminy: ' + openCount;
        statsList.appendChild(liOpen);

        const liReserved = document.createElement('li');
        liReserved.textContent = 'Zarezerwowane wizyty (oczekujące): ' + reservedCount;
        statsList.appendChild(liReserved);

        const liAccepted = document.createElement('li');
        liAccepted.textContent = 'Zaakceptowane wizyty: ' + acceptedCount;
        statsList.appendChild(liAccepted);
    }
});