document.getElementById('focus-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const submitBtn = document.querySelector('.primary-btn');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Calculating...';
    submitBtn.disabled = true;

    try {
        const name = document.getElementById('name').value;
        const studyDuration = parseInt(document.getElementById('studyDuration').value);
        const distractionTime = parseInt(document.getElementById('distractionTime').value) || 0;

        // Perform calculation purely on the frontend (Serverless / Portable!)
        let score = 0.0;
        if (studyDuration > 0) {
            score = studyDuration / (studyDuration + distractionTime);
        }

        // Add a tiny artificial delay so the animation feels cooler
        await new Promise(r => setTimeout(r, 600));

        showResult(score);

    } catch (error) {
        console.error('Error fetching score:', error);
        alert('Failed to calculate focus score.');
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
    if (score > 0.8) {
        scoreMsg.textContent = "Highly Focused! You're in the zone. 🔥";
        scoreValue.style.color = "#10b981"; // green
    } else if (score > 0.5) {
        scoreMsg.textContent = "Moderate session. Try minimizing distractions! 👍";
        scoreValue.style.color = "#f59e0b"; // yellow
    } else {
        scoreMsg.textContent = "Low Focus! Too many distractions. 🚨";
        scoreValue.style.color = "#ef4444"; // red
    }
}
