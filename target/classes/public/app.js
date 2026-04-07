document.getElementById('focus-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const submitBtn = document.querySelector('.primary-btn');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Calculating...';
    submitBtn.disabled = true;

    try {
        const payload = {
            name: document.getElementById('name').value,
            studyDuration: parseInt(document.getElementById('studyDuration').value),
            distractionTime: parseInt(document.getElementById('distractionTime').value)
        };

        const response = await fetch('http://localhost:7070/api/focus', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        showResult(data.score);

    } catch (error) {
        console.error('Error fetching score:', error);
        alert('Failed to calculate focus score. Ensure the backend is running!');
    } finally {
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    }
});

function showResult(score) {
    const resultCard = document.getElementById('result-card');
    const scoreCircle = document.querySelector('.score-circle');
    const scoreValue = document.getElementById('score-value');
    const scoreMsg = document.getElementById('score-message');

    resultCard.classList.remove('hidden');
    resultCard.style.display = 'block';

    const roundedScore = score.toFixed(2);
    scoreValue.textContent = roundedScore;

    // Animate the circle
    const percentage = score * 100;
    let currentPercent = 0;
    
    const interval = setInterval(() => {
        if(currentPercent >= percentage) {
            clearInterval(interval);
        } else {
            currentPercent++;
            scoreCircle.style.background = `conic-gradient(var(--accent) ${currentPercent}%, #1e293b ${currentPercent}%)`;
        }
    }, 15);

    // Messages
    if (score >= 0.8) {
        scoreMsg.textContent = "Outstanding focus! You're in the zone.";
        scoreValue.style.color = "#10b981"; // green
    } else if (score >= 0.5) {
        scoreMsg.textContent = "Good session. Try minimizing distractions next time.";
        scoreValue.style.color = "#f59e0b"; // yellow
    } else {
        scoreMsg.textContent = "Too many distractions! Keep trying.";
        scoreValue.style.color = "#ef4444"; // red
    }
}
