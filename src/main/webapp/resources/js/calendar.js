    const todayDate = moment().startOf('day');
    const TODAY = todayDate.format('YYYY-MM-DD');

    // dom elements
    const eventDetails = document.getElementById("event-details");
    const eventTitle = document.getElementById("event-title");
    const eventDescription = document.getElementById("event-description");
    const eventDate = document.getElementById("event-date");
    const eventType = document.getElementById("event-type");
    const eventColor = document.getElementById("event-color");
    const eventOrganizers = document.getElementById("event-organizers");
    const eventImage = document.getElementById("event-image");

    const eventsJson = JSON.stringify(events)

    var eventId = null;

    const calendarEl = document.getElementById('calendar');
    const calendar = new FullCalendar.Calendar(calendarEl, {
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
        },

        height: 800,
        contentHeight: 780,
        aspectRatio: 3,

        nowIndicator: true,
        now: TODAY + 'T09:25:00', // just for demo

        views: {
            dayGridMonth: {buttonText: 'month'},
            timeGridWeek: {buttonText: 'week'},
            timeGridDay: {buttonText: 'day'}
        },

        initialView: 'dayGridMonth',
        initialDate: TODAY,

        editable: false,
        dayMaxEvents: true,
        navLinks: true,

        // all the events
        events: JSON.parse(eventsJson) || [],

        eventClick: function (info) {
            const event = info.event;
            eventId = event.id;
            const title = event.title;
            const backgroundColor = event.backgroundColor;
            const start = event.start;
            const end = event.end;
            const description = event.extendedProps.description;
            const type = event.extendedProps.type;
            const image = event.extendedProps.image;
            const organizers = event.extendedProps.organizers;

            eventTitle.textContent = title;
            eventDescription.innerHTML = parseMarkdown(description);
            eventDate.innerHTML = end ? `<span class="fw-normal">from</span> ${formatDate(start)} <span class="fw-normal">to</span> ${formatDate(end)}` : `<span class="fw-normal">from</span> ${formatDate(start)}`
            eventColor.style.backgroundColor = `${backgroundColor}`
            eventType.textContent = type;

            if (image) {
                eventImage.innerHTML = `<img alt="${title}" src="${image}" class="w-100 h-px-250 object-fit-cover" />`;
            } else {
                eventImage.innerHTML = `<img alt="${title}" src="https://www.google.com/url?sa=i&url=https%3A%2F%2Fen.m.wikipedia.org%2Fwiki%2FFile%3ANo_Image_Available.jpg&psig=AOvVaw3Vb98cEPvOKCqeLf_W1Obr&ust=1712936809599000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCLDrooXBuoUDFQAAAAAdAAAAABAE" class="w-100 h-px-250 object-fit-cover" />`;
            }

            // organizers details
            const div = document.createElement('div');
            organizers.forEach(organizer => {
                div.innerHTML += `<p class="mb-0 fw-medium">${organizer.name}</p>
                                                <p class="mb-0">${organizer.email}</p>`;
            })
            eventOrganizers.innerHTML = div.innerHTML;

            eventDetails.classList.remove('d-none');
        }
    });

    calendar.render();


// utilities
function formatDate(dateString) {
    const date = new Date(dateString);

    const months = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];
    const day = date.getDate();
    const month = months[date.getMonth()];
    const year = date.getFullYear();
    const hours = date.getHours();
    let minutes = date.getMinutes();
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    return day + " " + month + " " + year + " " + hours + ":" + minutes;

}

function parseMarkdown(text) {
    const converter = new showdown.Converter();
    return  converter.makeHtml(text);
}

//
function selectEventId() {
    console.log(eventId)

    document.getElementById("form-event:participedEventId").value = eventId;
    return true;
}
