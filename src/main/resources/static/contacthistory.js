const contactNameInput = document.getElementById('contactName');
const startDateInput = document.getElementById('startDate');
const endDateInput = document.getElementById('endDate');
const changeTypeSelect = document.getElementById('changeType');
const filterButton = document.getElementById('filterButton');
const historyTable = document.getElementById('history-body');

function fetchAllContactHistory() {
  const url = new URL('/history/contacts', window.location.origin);

  fetch(url.toString())
    .then(response => response.json())
    .then(data => {
      window.allContactHistoryData = data;
      filterAndDisplayData();
    })
    .catch(error => console.error('Error fetching contact history:', error));
}

function displayHistory(filteredData) {
  historyTable.innerHTML = '';

  filteredData.forEach(history => {
    const row = historyTable.insertRow();
    const contactNameCell = row.insertCell(0);
    const changeTypeCell = row.insertCell(1);
    const changeDetailsCell = row.insertCell(2);
    const changeTimestampCell = row.insertCell(3);

    contactNameCell.textContent = history.contactName;
    changeTypeCell.textContent = history.changeType;
    changeDetailsCell.textContent = history.changeDetails; 
    changeTimestampCell.textContent = new Date(history.changeTimestamp).toLocaleString();
  });

  if (filteredData.length === 0) {
    const row = historyTable.insertRow();
    const cell = row.insertCell(0);
    cell.textContent = 'No records found';
    cell.colSpan = 4; 
  }
}

function filterAndDisplayData() {
  const contactName = contactNameInput.value.trim().toLowerCase();
  const startDate = startDateInput.value ? new Date(startDateInput.value) : null;
  const endDate = endDateInput.value ? new Date(endDateInput.value) : null;
  const changeType = changeTypeSelect.value;

  const filteredData = window.allContactHistoryData.filter(history => {
    const historyDate = new Date(history.changeTimestamp);
    return (
      (contactName === '' || history.contactName.toLowerCase().includes(contactName)) &&
      (!startDate || historyDate >= startDate) &&
      (!endDate || historyDate <= endDate) &&
      (changeType === '' || history.changeType === changeType)
    );
  });

  displayHistory(filteredData);
}

filterButton.addEventListener('click', filterAndDisplayData);

fetchAllContactHistory();