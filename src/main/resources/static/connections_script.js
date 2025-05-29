async function createRoom() {
    const name = document.getElementById("name").value.trim();
    if (!name) return alert("Please enter your name.");

    const player = {
        name: name,
    };

    try {
        const response = await fetch('/create-room', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(player)
        });

        if (!response.ok) {
            throw new Error("Failed to create room.");
        }

        const data = await response.json();
        const uid = data.player.uid;
        sessionStorage.setItem("playerUid", uid);
        window.location.href = `/room/${data.roomId}`;
    } catch (err) {
        alert("Error: " + err.message);
    }
}

async function joinRoom() {
    const name = document.getElementById("name").value.trim();
    const roomId = document.getElementById("roomId").value.trim();
    if (!name || !roomId) return alert("Please enter your name and room ID.");

    const request = {
        roomId: roomId,
        playerToJoin: {
            name: name
        }
    };

    try {
        const response = await fetch('/join-room', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request)
        });

        if (response.ok) {
            const data = await response.json(); // Await the JSON
            console.log(data);
            const uid = data.player.uid; // Then access player.uid
            sessionStorage.setItem("playerUid", uid);
            window.location.href = `/room/${roomId}`;
        }
        else {
            const message = await response.text();
            alert("Join failed: " + message);
        }
    } catch (err) {
        alert("Error: " + err.message);
    }

}
