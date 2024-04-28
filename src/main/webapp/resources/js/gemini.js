async function generateText(textInput) {
    const apiKey = 'AIzaSyAP0KRFtpszd4ojwXQIrwwN1GNsdxd8VG4'; // Replace with your actual API key

    const requestBody = {
        contents: [
            {
                parts: [
                    {
                        text: textInput,
                    },
                ],
            },
        ],
    };
    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
    };

    const url = `https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=${apiKey}`;

    try {
        const response = await fetch(url, requestOptions);

        if (!response.ok) {
            throw new Error(`API request failed with status ${response.status}`);
        }

        const data = await response.json();
        const generatedText = data.candidates[0].content.parts[0].text;
        return generatedText;
    } catch (error) {
        console.error(error);
        return null;
    }
}


async function editWithAi(id) {
    const btnAi = document.getElementById(`btnAi_${id}`);
    const title = document.querySelector(`.title_${id}`);
    const description = document.querySelector(`.description_${id}`);

    if (title === null || title.value === "") return "provide a title first !!!";

    btnAi.classList.add("bg-primary", "text-white");
    btnAi.classList.remove("border");

    const prompt  = `based on this title : ${title.value}, create me an interesting description use html emojis for event within 10 line, and use markdown, consider escaping special characters for safe json parsing. use backslash to escape special characters, for the title of description dont make it size too big in font size.`;
    const generatedText = await generateText(prompt);
    btnAi.classList.remove("bg-primary", "text-white");
    btnAi.classList.add("border");


    description.value = generatedText;
}

