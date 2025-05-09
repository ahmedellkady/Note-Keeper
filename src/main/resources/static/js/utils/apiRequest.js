export async function apiRequest(endpoint, method = 'GET', body = null) {
  const headers = { 'Content-Type': 'application/json' };

  const options = {
    method,
    headers,
    ...(body && { body: JSON.stringify(body) }),
  };

  try {
    const response = await fetch(`/api/${endpoint}`, options);

    if (!response.ok) {
      const errorBody = await response.json().catch(() => ({}));
      throw new Error(errorBody.message || 'Unexpected error occurred');
    }

    if (response.status === 204) {
      return null;
    }

    return await response.json();
  } catch (error) {
    throw new Error(error.message || 'Network error');
  }
}