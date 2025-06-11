let stompClient = null;
let currentPuzzleWords = [];


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

    // Socket connection
    const socket = new SockJS('/websocket-connect');
    stompClient = StompJs.Stomp.over(socket);

    stompClient.reconnectDelay = 5000;

    stompClient.onConnect = (frame) => {
        console.log('Connected to WebSocket:', frame);

        // Subscription for player list updates
        stompClient.subscribe('/broadcast/room/' + roomId, (message) => {
            console.log('Message received on /broadcast/room:', message.body);
            const updatedPlayers = JSON.parse(message.body);
            updatePlayerListUI(updatedPlayers);
        });

        // Subscription for guess submit results
        stompClient.subscribe('/broadcast/room/' + roomId + '/submit', (message) => {

            const result = JSON.parse(message.body);
            console.log(JSON.stringify(result, null, 2));
            onSubmitResultReceived(result);
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
    console.log(players);
    const playerListElem = document.getElementById('player-list');
    playerListElem.innerHTML = '';
    players.forEach(player => {
        const li = document.createElement('li');
        li.textContent = player.name + " Guesses Remaining: " + player.guessesRemaining;
        playerListElem.appendChild(li);
    });
}

function copyGameCode() {
    const pathParts = window.location.pathname.split('/');
    const roomId = pathParts[pathParts.length - 1];

    navigator.clipboard.writeText(roomId)
        .then(() => {
            console.log('Code copied to clipboard', roomId);
            alert('Code copied to clipboard');
        })
        .catch(err => {
            console.error('Failed to copy code:', err);
        });
}

function renderPuzzle() {
    const pathParts = window.location.pathname.split('/');
    const roomId = pathParts[pathParts.length - 1]; // assumes URL like /room/abc123

    fetch(`/get-room-puzzle?roomId=${encodeURIComponent(roomId)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to load puzzle");
            }
            return response.json();
        })
        .then(puzzle => {
            const words = Object.values(puzzle.allPuzzleWords).flat();
            currentPuzzleWords = words;
            shuffle(words);
            createPuzzleGrid(words);
        })
        .catch(error => console.error('Error loading puzzle:', error));
}

function onTileSelected(tile)
{
    console.log("tile selected");
    const isSelected = tile.classList.contains('selected');
    const selectedTiles = document.querySelectorAll('.tile.selected');

    if (!isSelected && selectedTiles.length >= 4) {
        return;
    }

    // Toggle selection state
    tile.classList.toggle('selected');

    // Update visual styles
    const nowSelected = tile.classList.contains('selected');
    tile.style.background = nowSelected ? '#cce5ff' : '#fff';
    tile.style.borderColor = nowSelected ? '#007bff' : '#ccc';
}

function createPuzzleGrid(words)
{
    const container = document.getElementById('grid-container');
    container.innerHTML = ''; // Clear old grid if present

    const grid = document.createElement('div');
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
        tile.addEventListener('click', () => onTileSelected(tile));

        grid.appendChild(tile);
    });

    container.appendChild(grid);
}



function onSubmitClicked()
{
    const selectedTiles = Array.from(document.querySelectorAll('.tile.selected'))
        .map(tile => tile.innerText);

    const playerUID = sessionStorage.getItem('playerUid');
    const pathParts = window.location.pathname.split('/');
    const roomId = pathParts[pathParts.length - 1];

    if (selectedTiles.length !== 4) {
        return;
    }

    if (stompClient && stompClient.connected && playerUID) {
        stompClient.publish({
            destination: `/app/room/${roomId}/guess-submit`,
            body: JSON.stringify({
                player: playerUID,
                guess: selectedTiles
            })
        });
    }
}

function addHistoryEntry(historyJSON)
{
    const playerName = historyJSON.playerName;
    const guessArray = historyJSON.guess;
    const correct = historyJSON.correct;

    const historyList = document.getElementById('guess-history');

    const listItem = document.createElement('li');

    listItem.classList.add('history-entry'); // Optional for styling

    const playerCol = document.createElement('span');
    playerCol.classList.add('history-player');
    playerCol.textContent = playerName;

    const guessCol = document.createElement('span');
    guessCol.classList.add('history-guess');
    guessCol.textContent = guessArray.join(', ');

    listItem.appendChild(playerCol);
    listItem.appendChild(guessCol);

    if (correct)
        listItem.style.backgroundColor = "#90EE90";
    else
        listItem.style.backgroundColor = "#FF474C";

    historyList.appendChild(listItem);
}




function onSubmitResultReceived(resultJson)
{
    addHistoryEntry(resultJson.history);
    updatePlayerListUI(resultJson.playerState.players);
    // Update history
    // Update grid if guess correct
}


function shuffle(array)
{
    for (let i = array.length - 1; i > 0; i--)
    {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
}


// On tab/window close
window.addEventListener('beforeunload', () =>
{
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

function onShuffleClicked()
{
    shuffle(currentPuzzleWords);
    createPuzzleGrid(currentPuzzleWords);
}


window.addEventListener('DOMContentLoaded', () =>
{
    connectToRoom();
    document.getElementById('shuffle-btn').addEventListener('click', onShuffleClicked);
    renderPuzzle();
});
