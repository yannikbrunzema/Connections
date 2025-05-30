let stompClient = null;

function renderPuzzle() {
    fetch('/get-puzzle')
        .then(response => response.json())
        .then(puzzle => {
            const words = Object.values(puzzle.categories).flat();
            shuffle(words);
            createPuzzleGrid(words);
        })
        .catch(error => console.error('Error loading puzzle:', error));
}

function createPuzzleGrid(words) {
    const main = document.querySelector('main');
    const grid = document.createElement('div');

    // Clear existing grid if any
    const oldGrid = document.getElementById('puzzle-grid');
    if (oldGrid) {
        oldGrid.remove();
    }

    grid.id = 'puzzle-grid';
    grid.style.display = 'grid';
    grid.style.gridTemplateColumns = 'repeat(4, 120px)';
    grid.style.gridGap = '1rem';
    grid.style.justifyContent = 'center';
    grid.style.marginTop = '2rem';

    words.forEach(word => {
        const tile = document.createElement('div');
        tile.className = 'tile';
        tile.innerText = word;
        tile.style.background = '#fff';
        tile.style.border = '2px solid #ccc';
        tile.style.borderRadius = '8px';
        tile.style.padding = '1rem';
        tile.style.textAlign = 'center';
        tile.style.cursor = 'pointer';
        tile.style.transition = 'background 0.2s, border-color 0.2s';
        tile.style.userSelect = 'none';

        tile.addEventListener('click', () => {
            tile.classList.toggle('selected');
            tile.style.background = tile.classList.contains('selected') ? '#cce5ff' : '#fff';
            tile.style.borderColor = tile.classList.contains('selected') ? '#007bff' : '#ccc';
        });

        grid.appendChild(tile);
    });

    main.appendChild(grid);
}

function shuffle(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
}

function connectToRoom() {
    const pathParts = window.location.pathname.split('/');
    const roomId = pathParts[pathParts.length - 1];
    console.log('Room ID:', roomId);

    // === Fetch initial players via HTTP GET ===
    fetch(`/get-room-members?roomId=${encodeURIComponent(roomId)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch initial player list');
            }
            return response.json();
        })
        .then(players => {
            console.log('Initial players:', players);
            updatePlayerListUI(players);
        })
        .catch(error => {
            console.error('Error fetching players:', error);
        });

    // === Use SockJS for WebSocket fallback compatibility ===
    const socket = new SockJS('/websocket-connect');
    stompClient = StompJs.Stomp.over(socket);

    stompClient.reconnectDelay = 5000; // auto reconnect after 5 seconds

    stompClient.onConnect = (frame) => {
        console.log('Connected to WebSocket:', frame);
        stompClient.subscribe('/broadcast/room/' + roomId, (message) => {
            const updatedPlayers = JSON.parse(message.body);
            console.log('Room players updated:', updatedPlayers);
            updatePlayerListUI(updatedPlayers);
        });
    };

    stompClient.onWebSocketError = (error) => {
        console.error('WebSocket error:', error);
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error:', frame.headers['message']);
        console.error('Additional details:', frame.body);
    };

    stompClient.activate();
}

function updatePlayerListUI(players) {
    const playerListElem = document.getElementById('player-list');
    playerListElem.innerHTML = '';
    players.forEach(player => {
        const li = document.createElement('li');
        li.textContent = player.name + " Guesses Remaining: " + player.guessesRemaining;
        playerListElem.appendChild(li);
    });
}

// On tab/window close
window.addEventListener('beforeunload', () => {
    const playerUID = sessionStorage.getItem('playerUid');
    const pathParts = window.location.pathname.split('/');
    const roomId = pathParts[pathParts.length - 1];

    if (playerUID) {
        const payload = {
            playerUID: playerUID,
            roomId: roomId
        };

        // Use fetch with keepalive
        fetch('/leave-room', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload),
            keepalive: true
        });
    }
});

window.addEventListener('DOMContentLoaded', () => {
    connectToRoom();
    // renderPuzzle();
});
